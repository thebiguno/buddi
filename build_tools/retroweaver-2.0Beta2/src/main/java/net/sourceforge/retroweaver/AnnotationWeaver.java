package net.sourceforge.retroweaver;

import net.sourceforge.retroweaver.runtime.java.lang.annotation.AIB;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

  /**
   * Checks for any annotations and, if found, preps the class with a field to store them in at runtime.
   *
   * Implementation Notes:
   *
   * In Java 1.5:
   * ------------
   *
   * RuntimeVisibleAnnotations attributes are in ClassFile, field_info, and method_info structures
   * RuntimeVisibleParameterAnnotations attributes are in method_info
   * AnnotationDefault attributes are in method_info for methods that are of an Annotation class
   * Package level annotations go in <package-name>.package-info.class
   *
   * For Retroweaver:
   * ----------------
   *
   * The "retroweaved" layout for a class which has been directly annotated, or whose
   * members (constructors,methods,method-parameters,fields) have been annotated is as
   * follows:
   *
   * class AnnotatedClass {
   *   public static transient net.sourceforge.retroweaver.runtime.java.lang.annotation.AIB _rw_annotations$;
   * }
   *
   * Interfaces (which includes annotation classes) are special, in that you can't store a public static (non-final) 
   * member in them, so we generate an "annotations" class for them, named [interface]_rw_annotations$.class. 
   * This class has the annotations field as its only member.
   *
   * Retroweaver only creates this member when the particular entity has been annotated
   * with at least one annotation with RetentionPolicy.RUNTIME or is an Annotation class
   * that has method defaults.
   *
   * Note: The annotation data itself is actually stored within the class file in the same format 
   * as it always is. What's happening here is that Retroweaver loads the class file at runtime,
   * parses the annotation data, and stores it in these members that were introduced at weave time.
   * (We're assuming that we can always get to the .class file via the class loader - this has been
   * tested for applications, applets, and JWS).
   *
   * Note: This field is public, because Retroweaver's runtime requires access to it. We could have made it
   * private and then used setAccessible() to get at it, but that wouldn't play well with many SecurityManagers. 
   *
   */

class AnnotationWeaver extends ClassAdapter {

  private boolean annotationPresent;
  private boolean isInterface;

  public AnnotationWeaver( ClassVisitor visitor ) {
    super( visitor );
  }

  public void visit( int version, int access, String name, String signature, String superName, String[] interfaces ) {
    isInterface = ( access & Opcodes.ACC_INTERFACE ) == Opcodes.ACC_INTERFACE;

	super.visit(version, access, name, signature, superName, interfaces);
  }

  public AnnotationVisitor visitAnnotation( String desc, boolean visible ) {
    if ( visible ) {
      annotationPresent = true;
    }
    return super.visitAnnotation( desc, visible );
  }

  public void visitEnd() {
    if (annotationPresent && !isInterface) {
      addAnnotationsMember();
    }
    super.visitEnd();
  }

  private static final String AIB_FIELD_DESCRIPTOR = "Lnet/sourceforge/retroweaver/runtime/java/lang/annotation/AIB;";

  private void addAnnotationsMember() {
    final int flags = Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_SYNTHETIC;
    super.visitField( flags, AIB.ANNOTATIONS_FIELD, AIB_FIELD_DESCRIPTOR, null, null );
  }

  public FieldVisitor visitField( int access, String name, String desc, String signature, Object value ) {
	if (name.equals(AIB.ANNOTATIONS_FIELD)) {
		// remove synthetic field from previous weaving.
		return null;
	}
    final FieldVisitor v = super.visitField( access, name, desc, signature, value );
    return new FieldVisitor() {
      public AnnotationVisitor visitAnnotation( String desc, boolean visible ) {
        if ( visible ) {
          annotationPresent = true;
        }
        return v.visitAnnotation( desc, visible );
      }
      public void visitAttribute( Attribute attr ) {
        v.visitAttribute( attr );
      }
      public void	visitEnd() {
        v.visitEnd();
      }
    };
  }

  public MethodVisitor visitMethod( int access, String name, String desc, String signature, String[] exceptions ) {
	  MethodVisitor mv = super.visitMethod( access, name, desc, signature, exceptions );
	  return (mv == null)? null : new MethodAnnotationVisitor(mv);
  }

  class MethodAnnotationVisitor extends MethodAdapter {
    public MethodAnnotationVisitor( MethodVisitor mv ) {
      super( mv );
    }

    public void visitCode() {
        mv.visitCode();
    }

    public AnnotationVisitor visitAnnotation( String desc, boolean visible ) {
      if ( visible ) {
        annotationPresent = true;
      }
      return super.visitAnnotation( desc, visible );
    }
    public AnnotationVisitor visitAnnotationDefault() {
      annotationPresent = true;
      return super.visitAnnotationDefault();
    }
    public AnnotationVisitor visitParameterAnnotation( int parameter, String desc, boolean visible ) {
      if ( visible ) {
        annotationPresent = true;
      }
      return super.visitParameterAnnotation( parameter, desc, visible );
    }
  }
}


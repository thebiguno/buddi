
package net.sourceforge.retroweaver.runtime.java.lang.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import net.sourceforge.retroweaver.runtime.java.lang.Class_;
import net.sourceforge.retroweaver.runtime.java.lang.Enum;

/**
 * The Annotation Information Block.
 *
 * This is the runtime data structure that holds all of the annotation data in
 * a form that Retroweaver's runtime can use easily. At weave time, we create 
 * a public static transient field named [ANNOTATIONS_FIELD] of this type. At
 * runtime, we parse the class file, read the annotation data, and populate this
 * data structure.
 *
 * ( Method parameter annotations appear in the same order as method parameters,
 *  and each parameter gets its own list of Annotations )
 *
 * @author Toby Reyelts
 *
 */
public class AIB implements ClassVisitor {

  /** The field containing the declared annotations for a class. */
  public static final String ANNOTATIONS_FIELD = "_rw_annotations$";

  private static final AIB EMPTY_AIB = new AIB();

  private Class class_;
  private Map<String,Annotation> classAnnotations;
  private Map<String,Map<String,Annotation>> methodAnnotations;
  private Map<String,ArrayList<Map<String,Annotation>>> methodParameterAnnotations;
  private Map<String,Map<String,Annotation>> fieldAnnotations;
  private Map<String,Annotation> cachedInheritedClassAnnotations; // NOPMD by xlv
  private Map<String,Object> cachedMethodDefaults; // NOPMD by xlv

  private AIB() {
    classAnnotations = new HashMap<String,Annotation>();
    methodAnnotations = new HashMap<String,Map<String,Annotation>>();
    methodParameterAnnotations = new HashMap<String,ArrayList<Map<String,Annotation>>>();
    fieldAnnotations = new HashMap<String,Map<String,Annotation>>();
  }

  private AIB(Class c) {
		this();
		this.class_ = c;

		readClassStream(c.getName(), this);
	}

	private void readClassStream(final String name, final ClassVisitor cv) {
		String resource = "/" + name.replace('.', '/') + ".class";
		InputStream classStream = class_.getResourceAsStream(resource);
		try {
			ClassReader r = new ClassReader(classStream);
			r.accept(cv, ClassReader.SKIP_CODE + ClassReader.SKIP_DEBUG
					+ ClassReader.SKIP_FRAMES);

		} catch (IOException e) {
			// Shouldn't generally happen
			throw new AnnotationFormatError(
					"[Retroweaver] Unable to read annotation data for: " + name, e);
		} finally {
			try {
				if (classStream != null) {
					classStream.close();
				}
			} catch (IOException e) { // NOPMD by xlv
			}
		}
	}

  private Map<String,Annotation> getInheritedClassAnnotations() {
	  // FIXME: threading
	  if (class_ == null) {
		  return EMPTY_ANNOTATION_MAP;
	  }

    if ( cachedInheritedClassAnnotations != null ) {
      return cachedInheritedClassAnnotations;
    }

    final Map<String,Annotation> annotations = new HashMap<String,Annotation>( classAnnotations );
    Class currentClass = class_;
    final Class inheritedAnnotation = Inherited.class;
    
    while ( true ) {
      currentClass = currentClass.getSuperclass();

      if ( currentClass == null ) {
        break;
      }

      for (Annotation annotation : Class_.getDeclaredAnnotations(currentClass)) {
        final Class annotationClass = annotation.getClass();

        if ( Class_.isAnnotationPresent( annotationClass, inheritedAnnotation ) ) {
          annotations.put( annotationClass.getName(), annotation );
        }
      }
    }
  
    cachedInheritedClassAnnotations = annotations;

    return cachedInheritedClassAnnotations;
  }

  private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[]{};

  private static final Annotation[][] EMPTY_ANNOTATION_ARRAY_ARRAY = new Annotation[][]{};

  private static final Map<String,Annotation> EMPTY_ANNOTATION_MAP = new HashMap<String,Annotation>();

  public Annotation[] getClassAnnotations() {
	  return getInheritedClassAnnotations().values().toArray( EMPTY_ANNOTATION_ARRAY );
  }

  public Annotation[] getDeclaredClassAnnotations() {
	  return classAnnotations.values().toArray( EMPTY_ANNOTATION_ARRAY );
  }

  public <T extends Annotation> T getClassAnnotation(final Class<T> annotationType) {
	  return (T) getInheritedClassAnnotations().get(annotationType.getName());
  }

  public Annotation[] getFieldAnnotations(final String fieldName) {
	  final Map<String,Annotation> annotations = fieldAnnotations.get( fieldName );
      return annotations.values().toArray( EMPTY_ANNOTATION_ARRAY );
  }

  public <T extends Annotation> T getFieldAnnotation(final String fieldName, final Class<T> annotationType) {
	  final Map<String,Annotation> annotations = fieldAnnotations.get( fieldName );
	  return (T) annotations.get(annotationType.getName());
  }

  private String getMethodIdentifier(final String methodName, final Class[] parameterTypes, final Class returnType) {
	    final StringBuilder b = new StringBuilder(methodName);
	    b.append('(');
	    for (Class c: parameterTypes) {
	    	b.append(Type.getDescriptor(c));
	    }
	    b.append(')').append(Type.getDescriptor(returnType));
	    return b.toString();
	  
  }
  public Annotation[] getMethodAnnotations(final String methodName, final Class[] parameterTypes, final Class returnType) {
	  final Map<String,Annotation> annotations = methodAnnotations.get(getMethodIdentifier(methodName, parameterTypes, returnType));
      return annotations.values().toArray( EMPTY_ANNOTATION_ARRAY );	  
  }

  public <T extends Annotation> T getMethodAnnotation(final String methodName, final Class[] parameterTypes, final Class returnType, final Class<T> annotationType) {
	  final Map<String,Annotation> annotations = methodAnnotations.get(getMethodIdentifier(methodName, parameterTypes, returnType));
	  return (T) annotations.get(annotationType.getName());
  }

  private Map<String, Object> getMethodDefaults() {
	  assert(class_.isAnnotation());

	  if (cachedMethodDefaults == null) {
		DefaultValueVisitor v = new DefaultValueVisitor();
		cachedMethodDefaults = v.parseAttributes(class_.getName());
	  }

	  return cachedMethodDefaults;
  }

  public Object getDefaultValue(final String methodName) {
	  assert(class_.isAnnotation());

	  return getMethodDefaults().get(methodName);
  }

  public Annotation[][] getMethodParameterAnnotations(final String methodName, final Class[] parameterTypes, final Class returnType) {
	  ArrayList<Map<String, Annotation>> annotations = methodParameterAnnotations.get(getMethodIdentifier(methodName, parameterTypes, returnType));

	  if (annotations == null) {
		  return EMPTY_ANNOTATION_ARRAY_ARRAY; // NOPMD by xlv
	  }

	  if (annotations.size() != parameterTypes.length) {
		  throw new AnnotationFormatError("inconsistent parameter count");
	  }

	  Annotation[][] a = new Annotation[parameterTypes.length][];

	  for(int i = 0; i < parameterTypes.length; i++) {
		  Map<String, Annotation> map = annotations.get(i);
		  
		  a[i] = map.values().toArray(EMPTY_ANNOTATION_ARRAY);
	  }
	  
	  return a;
  }

	private static final Map<Class, AIB> interfaceDescriptors = new Hashtable<Class, AIB>();

	/**
	 * Returns the AIB for the class.
	 */
	public static AIB getAib(final Class c) {
		synchronized (c) {
			if (c.isInterface()) {
				AIB aib = interfaceDescriptors.get(c);
				if (aib == null) {
					aib = new AIB(c);
					interfaceDescriptors.put(c, aib);
				}
				return aib;
			} else {
				try {
					Field f = c.getField(ANNOTATIONS_FIELD);
					f.setAccessible(true);
					AIB aib = (AIB) f.get(null);
					if (aib == null) {
						aib = new AIB(c);
						f.set(null, aib);
					}
					return aib;
				} catch (NoSuchFieldException e) {
					// No annotation data for this class
					return EMPTY_AIB;
				} catch (IllegalAccessException e) {
					// Shouldn't ever happen, because the annotations field
					// should be public
					throw new AnnotationFormatError(
							"Retroweaver needs permission to read the fields of Annotation classes.",
							e);
				}
			}

		}
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (!visible) { return EMPTY_VISITOR; }

		return new TopLevelAnnotation(desc, classAnnotations);
	}

	public FieldVisitor visitField(final int access, final String fieldName,
			final String desc, final String signature, final Object value) {
		return new FieldVisitor() {

			Map<String,Annotation> annotations = new HashMap<String,Annotation>();
			
			public AnnotationVisitor visitAnnotation(String desc,
					boolean visible) {
				if (!visible) { return EMPTY_VISITOR; }

				return new TopLevelAnnotation(desc, annotations);
			}

			public void visitAttribute(Attribute attr) {
				// EMPTY
			}

			public void visitEnd() {
				fieldAnnotations.put(fieldName, annotations);
			}
		};
	}

	public MethodVisitor visitMethod(final int access, final String methodName,
			final String desc, final String signature, final String[] exceptions) {
		return new MethodAdapter(EMPTY_VISITOR) {

			Map<String,Annotation> ma = new HashMap<String,Annotation>();
			ArrayList<Map<String, Annotation>> pa = new ArrayList<Map<String, Annotation>>();

			public AnnotationVisitor visitAnnotationDefault() {
				return EMPTY_VISITOR;
			}

			public AnnotationVisitor visitAnnotation(String desc,
					boolean visible) {
				if (!visible) { return EMPTY_VISITOR; }

				return new TopLevelAnnotation(desc, ma);
			}

			public AnnotationVisitor visitParameterAnnotation(int parameter,
					String desc, boolean visible) {
				if (!visible) { return EMPTY_VISITOR; }

				Map<String, Annotation> map;
				if (parameter < pa.size()) {
					map = pa.get(parameter);
				} else {
					map = new HashMap<String, Annotation>();
					pa.add(parameter, map);
				}

				return new TopLevelAnnotation(desc, map);
			}

			public void visitEnd() {
				String name = methodName + desc;
				methodAnnotations.put(name, ma);
				methodParameterAnnotations.put(name, pa);
			}
		};
	}

	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		// EMPTY
	}

	public void visitSource(String source, String debug) {
		// EMPTY
	}

	public void visitOuterClass(String owner, String name, String desc) {
		// EMPTY
	}

	public void visitAttribute(Attribute attr) {
		// EMPTY
	}

	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
		// EMPTY
	}

	public void visitEnd() {
		// EMPTY
	}

	class DefaultValueVisitor implements ClassVisitor {
 
		private final Map<String,Object> attributes = new HashMap<String,Object>();

		Map<String,Object> parseAttributes(String className) {
			readClassStream(className, this);

			return attributes;
		}

		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			return EMPTY_VISITOR;
		}

		public FieldVisitor visitField(final int access, final String name,
				final String desc, final String signature, final Object value) {
			return EMPTY_VISITOR;
		}

		public MethodVisitor visitMethod(final int access, final String methodName,
				final String desc, final String signature, final String[] exceptions) {
			return new MethodAdapter(EMPTY_VISITOR) {

				public AnnotationVisitor visitAnnotationDefault() {
					// remove leading ()
					String type = desc.substring(2);

					return new DefaultAnnotation(methodName, type, attributes);
				}

				public AnnotationVisitor visitAnnotation(String desc,
						boolean visible) {
					return EMPTY_VISITOR;
				}

				public AnnotationVisitor visitParameterAnnotation(int parameter,
						String desc, boolean visible) {
					return EMPTY_VISITOR;
				}
			};
		}

		public void visit(int version, int access, String name, String signature,
				String superName, String[] interfaces) {
			// EMPTY
		}

		public void visitSource(String source, String debug) {
			// EMPTY
		}

		public void visitOuterClass(String owner, String name, String desc) {
			// EMPTY
		}

		public void visitAttribute(Attribute attr) {
			// EMPTY
		}

		public void visitInnerClass(String name, String outerName,
				String innerName, int access) {
			// EMPTY
		}

		public void visitEnd() {
			// EMPTY
		}
	}

	abstract class AbstractAnnotationVisitor implements AnnotationVisitor {

		protected final AbstractAnnotationVisitor parent;

		protected final String className;

		AbstractAnnotationVisitor(AbstractAnnotationVisitor parent, String className) {
			this.parent = parent;
			this.className = className;
		}

		protected Class getClass(String name) {
			try {
				return Class.forName(name, true, class_.getClassLoader());
			} catch (ClassNotFoundException e) {
				throw new AnnotationFormatError(
						"[Retroweaver] Unable to find class: " + name, e);
			}
		}

		protected Annotation createAnnotation(String className, Map<String, Object>attributes) {
			String internalName = Type.getType(className).getClassName();
			Class<? extends Annotation> type = (Class<? extends Annotation>) getClass(internalName);
			Annotation a = AnnotationImpl.createAnnotation(type, attributes);

			return a;
		}

		private Object getEnumValue(final String desc, final String value) {
			String name = Type.getType(desc).getClassName();
			Class c = getClass(name);
			return Enum.valueOf(c, value);
		}

		abstract void insertValue(String name, Object value);

		public void visit(String name, Object value) {
			Object v;
			if (value instanceof Type) {
				Type t = (Type) value;
				v = getClass(t.getClassName());
			} else {
				v = value;
			}

			insertValue(name, v);
		}

		public void visitEnum(String name, String desc, String value) {
			insertValue(name, getEnumValue(desc, value));
		}

		public AnnotationVisitor visitAnnotation(String name, String desc) {
			return new NestedAnnotation(this, desc);
		}

	}

	private class ArrayAnnotation extends AbstractAnnotationVisitor {

		ArrayAnnotation(AbstractAnnotationVisitor parent, String className, String type) {
			super(parent, className);
			this.type = type;
		}

		private final String type;

		private final List<Object> values = new LinkedList<Object>();

		void insertValue(String name, Object value) {
			values.add(value);
		}

		public AnnotationVisitor visitArray(String name) {
			throw new UnsupportedOperationException("Nested arrays are not allowed");
		}

		public void visitEnd() {
			Class c = getClass(type.replace('/', '.'));
			c = c.getComponentType();

			Object a = Array.newInstance(c, values.size());
			if (values.size() > 0) {
				a = values.toArray((Object[]) a);
			}

			parent.insertValue(className, a);
		}
	}

	private class NestedAnnotation extends AbstractAnnotationVisitor {		

		NestedAnnotation(AbstractAnnotationVisitor parent, String className) {
			super(parent, className);
		}

		private final Map<String, Object> attributes = new HashMap<String, Object>();

		void insertValue(String name, Object value) {
			attributes.put(name, value);
		}

		public AnnotationVisitor visitArray(String name) {
			throw new UnsupportedOperationException();
		}

		public void visitEnd() {
			Annotation annotation = createAnnotation(className, attributes);

			parent.insertValue(className, annotation);
		}

	}

	private class DefaultAnnotation extends AbstractAnnotationVisitor {
		
		DefaultAnnotation(String className, String type, Map<String, Object> attributes) {
			super(null, className);
			this.type = type;
			this.attributes = attributes;
		}

		private final String type;

		private final Map<String, Object> attributes;
		
		void insertValue(String name, Object value) {
			attributes.put(className, value);
		}

		public AnnotationVisitor visitArray(String name) {
			return new ArrayAnnotation(this, className, type);
		}

		public AnnotationVisitor visitAnnotation(String name, String desc) {
			return new NestedAnnotation(this, className);
		}

		public void visitEnd() {
		}

	}

	private class TopLevelAnnotation extends AbstractAnnotationVisitor {

		TopLevelAnnotation(String className, Map<String,Annotation> annotations) {
			super(null, className);

			this.annotations = annotations;

			// first get default values
			// then visitor methods are used to fill the custom settings
			String type = Type.getType(className).getClassName();
			attributes = new HashMap<String, Object>(getAib(getClass(type)).getMethodDefaults());
		}

		private final Map<String,Annotation> annotations;

		private final Map<String, Object> attributes;

		private String getClassNameFromInternalName(final String name) {
			if (name.charAt(0) != 'L') {
				return name;
			}

			return name.replace('/', '.').substring(1, name.length()-1);
		}

		void insertValue(String name, Object value) {
			String key = getClassNameFromInternalName(name);
			attributes.put(key, value);
		}

		public AnnotationVisitor visitArray(String name) {
			try {
				String type = Type.getType(className).getClassName();
				Method m = Class.forName(type).getMethod(name, new Class[0]);
				type = m.getReturnType().getName();
				return new ArrayAnnotation(this, name, type);
			} catch (Exception e) {
				throw new AnnotationFormatError(e);
			}
		}

		public void visitEnd() {
			Annotation annotation = createAnnotation(className, attributes);

			String key = getClassNameFromInternalName(className);
			annotations.put(key, annotation);
		}

	}
	
	private static final AIBEmptyVisitor EMPTY_VISITOR = new AIBEmptyVisitor();

	private static final class AIBEmptyVisitor implements ClassVisitor, FieldVisitor,
			MethodVisitor, AnnotationVisitor {

		public void visit(final int version, final int access,
				final String name, final String signature,
				final String superName, final String[] interfaces) {
		}

		public void visitSource(final String source, final String debug) {
		}

		public void visitOuterClass(final String owner, final String name,
				final String desc) {
		}

		public AnnotationVisitor visitAnnotation(final String desc,
				final boolean visible) {
			return this;
		}

		public void visitAttribute(final Attribute attr) {
		}

		public void visitInnerClass(final String name, final String outerName,
				final String innerName, final int access) {
		}

		public FieldVisitor visitField(final int access, final String name,
				final String desc, final String signature, final Object value) {
			return this;
		}

		public MethodVisitor visitMethod(final int access, final String name,
				final String desc, final String signature,
				final String[] exceptions) {
			return this;
		}

		public void visitEnd() {
		}

		public AnnotationVisitor visitAnnotationDefault() {
			return this;
		}

		public AnnotationVisitor visitParameterAnnotation(final int parameter,
				final String desc, final boolean visible) {
			return this;
		}

		public void visitCode() {
		}

		public void visitFrame(final int type, final int nLocal,
				final Object[] local, final int nStack, final Object[] stack) {
		}

		public void visitInsn(final int opcode) {
		}

		public void visitIntInsn(final int opcode, final int operand) {
		}

		public void visitVarInsn(final int opcode, final int var) {
		}

		public void visitTypeInsn(final int opcode, final String desc) {
		}

		public void visitFieldInsn(final int opcode, final String owner,
				final String name, final String desc) {
		}

		public void visitMethodInsn(final int opcode, final String owner,
				final String name, final String desc) {
		}

		public void visitJumpInsn(final int opcode, final Label label) {
		}

		public void visitLabel(final Label label) {
		}

		public void visitLdcInsn(final Object cst) {
		}

		public void visitIincInsn(final int var, final int increment) {
		}

		public void visitTableSwitchInsn(final int min, final int max,
				final Label dflt, final Label labels[]) {
		}

		public void visitLookupSwitchInsn(final Label dflt, final int keys[],
				final Label labels[]) {
		}

		public void visitMultiANewArrayInsn(final String desc, final int dims) {
		}

		public void visitTryCatchBlock(final Label start, final Label end,
				final Label handler, final String type) {
		}

		public void visitLocalVariable(final String name, final String desc,
				final String signature, final Label start, final Label end,
				final int index) {
		}

		public void visitLineNumber(final int line, final Label start) {
		}

		public void visitMaxs(final int maxStack, final int maxLocals) {
		}

		public void visit(final String name, final Object value) {
		}

		public void visitEnum(final String name, final String desc,
				final String value) {
		}

		public AnnotationVisitor visitAnnotation(final String name,
				final String desc) {
			return this;
		}

		public AnnotationVisitor visitArray(final String name) {
			return this;
		}
	}

}


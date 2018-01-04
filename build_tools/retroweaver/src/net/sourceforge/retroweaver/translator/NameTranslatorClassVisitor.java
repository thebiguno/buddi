package net.sourceforge.retroweaver.translator;

import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class NameTranslatorClassVisitor extends ClassAdapter {

	private final NameTranslator translator;

	public NameTranslatorClassVisitor(final ClassVisitor classVisitor, final NameTranslator translator) {
		super(classVisitor);
		this.translator = translator;
	}

	private Set<String> visitedMethods;

	private String className;

	public void visit(final int version, final int access, final String name,
			final String signature, final String superName,
			final String[] interfaces) {
		final String newSuperName = translator.getClassMirrorTranslatation(superName);

		String newInterfaces[] = new String[interfaces.length];
		for (int i = 0; i < interfaces.length; i++) {
			newInterfaces[i] = translator.getClassMirrorTranslatation(interfaces[i]);
		}

		className = name;
		visitedMethods = new HashSet<String>();

		super.visit(version, access, name, signature, newSuperName, newInterfaces);
	}

	public FieldVisitor visitField(final int access, final String name,
			final String desc, final String signature, final Object value) {
		return super.visitField(access, name,
								translator.getClassMirrorTranslatation(desc),
								translator.getClassMirrorTranslatation(signature), value);
	}

	public MethodVisitor visitMethod(final int access, final String name,
			final String desc, final String signature, final String[] exceptions) {

		final String newDesc = translator.translateMethodDescriptor(desc);
		final String fullDesc = name + newDesc;
		if (visitedMethods.contains(fullDesc)) {
			throw new TranslatorException(
					"Duplicate method after name translation in class "
							+ className + ": " + name + ' ' + newDesc);
		}
		visitedMethods.add(fullDesc);

		MethodVisitor mv = super.visitMethod(access, name, newDesc,
				translator.getClassMirrorTranslatation(signature), exceptions);
		return (mv == null)?null:new MethodTranslator(mv);
	}

    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
    	final String newDesc = translateAnnotationDescriptor(desc);
        return new AnnotationTranslator(cv.visitAnnotation(newDesc, visible));
    }

	private String translateAnnotationDescriptor(final String name) {
		if ((name == null) || (name.length() == 0)) {
			return name;
		}

		if ((name.charAt(0) != 'L') || (name.charAt(name.length()-1) != ';')) {
			return name;
		}

		return translator.translateDescriptor(name);
	}

	private class MethodTranslator extends MethodAdapter {
		MethodTranslator(MethodVisitor methodVisitor) {
			super(methodVisitor);
		}

		public void visitTypeInsn(final int opcode, final String desc) {
			super.visitTypeInsn(opcode, translator.getClassMirrorTranslatation(desc));
		}

		public void visitFieldInsn(final int opcode, final String owner,
				final String name, final String desc) {
			super.visitFieldInsn(opcode,
									translator.getClassMirrorTranslatation(owner), name,
									translator.getClassMirrorTranslatation(desc));
		}

		public void visitMethodInsn(final int opcode, final String owner,
				final String name, final String desc) {

			// Special case method invocations for name translation.
			// Specifically to deal with methods mirrors.
			String newOwner = owner;
			int newOpcode = opcode;
			String newDesc = desc;

			final Mirror mirror = translator.getMirror(owner);

			if (mirror.isClassMirror()) {
				newOwner = translator.translate(owner);
			} else if ("<init>".equals(name)&&(opcode == Opcodes.INVOKESPECIAL)) {
				/* Look for an equivalent constructor. For instance,
				 * INVOKESPECIAL, "java/math/BigDecimal", "<init>", "(I)V")
				 * will be transformed as
				 * INVOKESTATIC, "../BigDecimal_", "BigDecimal", "(I)Ljava/math/BigDecimal;"
				 * 
				 * the previously constructed object was on top of the stack and the mirror
				 * function has put its result on top, so a SWAP and POP are issued to
				 * discard the previously constructed object and store the new one instead.
				 */
				String constructorDesc = desc.substring(0, desc.length()-1) + 'L' + owner + ';';
				String constructorName;
				int i = owner.lastIndexOf('/');
				if (i == -1) {
					constructorName = owner;
				} else {
					constructorName = owner.substring(i+1);
				}
				if (mirror.hasMethod(owner, constructorName, constructorDesc, Opcodes.INVOKESPECIAL)) {
					newOwner = translator.translate(owner);

					super.visitMethodInsn(Opcodes.INVOKESTATIC, newOwner, constructorName,
							constructorDesc);

					super.visitInsn(Opcodes.SWAP);
					super.visitInsn(Opcodes.POP);
					return;
				}
			} else if (mirror.hasMethod(owner, name, desc, opcode)) {
				newOwner = translator.translate(owner);
				newOpcode = Opcodes.INVOKESTATIC;

				// We have to insert the owner into the arguments of the
				// descriptor
				if (opcode == Opcodes.INVOKEVIRTUAL) {
					final Type[] argTypes = Type.getArgumentTypes(desc);
					final Type[] newArgTypes = new Type[argTypes.length + 1];
					newArgTypes[0] = Type.getType("L" + owner + ";");
					System.arraycopy(argTypes, 0, newArgTypes, 1, argTypes.length);
					newDesc = Type.getMethodDescriptor(
							Type.getReturnType(desc), newArgTypes);
				}
			}

			super.visitMethodInsn(newOpcode, newOwner, name,
									translator.translateMethodDescriptor(newDesc));
		}

		public void visitTryCatchBlock(final Label start, final Label end,
				final Label handler, final String type) {
			super.visitTryCatchBlock(start, end, handler, translator.translate(type));
		}

		public void visitLocalVariable(final String name, final String desc,
				final String signature, final Label start, final Label end,
				final int index) {
			super.visitLocalVariable(name, translator.translate(desc),
					translator.getClassMirrorTranslatation(signature), start,
					end, index);
		}
		
	    public AnnotationVisitor visitAnnotationDefault() {
	        return new AnnotationTranslator(mv.visitAnnotationDefault());
	    }

	    public AnnotationVisitor visitAnnotation(
	        final String desc,
	        final boolean visible)
	    {
	        return new AnnotationTranslator(mv.visitAnnotation(desc, visible));
	    }

	    public AnnotationVisitor visitParameterAnnotation(
	        final int parameter,
	        final String desc,
	        final boolean visible)
	    {
	        return new AnnotationTranslator(mv.visitParameterAnnotation(parameter, desc, visible));
	    }

	}

	private class AnnotationTranslator implements AnnotationVisitor {
		
		private final AnnotationVisitor av;
		
		AnnotationTranslator(final AnnotationVisitor av) {
			this.av = av;
		}

		public void visit(final String name, final Object value) {
			final String newName = translateAnnotationDescriptor(name);
			av.visit(newName, value);
		}

		public void visitEnum(final String name, final String desc, final String value) {
			final String newName = translateAnnotationDescriptor(name);
			final String newDesc = translateAnnotationDescriptor(desc);
	    	av.visitEnum(newName, newDesc, value);
	    }

		public AnnotationVisitor visitAnnotation(final String name, final String desc) {
			final String newName = translateAnnotationDescriptor(name);
	    	return new AnnotationTranslator(av.visitAnnotation(newName, desc));
	    }

		public AnnotationVisitor visitArray(final String name) {
			final String newName = translateAnnotationDescriptor(name);
	    	return new AnnotationTranslator(av.visitArray(newName));
	    }

		public void visitEnd() {
	    	av.visitEnd();
	    }

	}
}

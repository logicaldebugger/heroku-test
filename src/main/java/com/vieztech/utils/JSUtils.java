package com.vieztech.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class JSUtils {
	@SuppressWarnings({ "rawtypes", "unchecked" }) // suppressing because the
													// class will only be
													// scanned via reflection
	public static String getClassesAsJS(final Class... clazzes) throws IOException {
		final StringBuilder sb = new StringBuilder();
		sb.append("<script>\n");
		for (final Class<Object> clazz : clazzes) {
			sb.append("\n").append(JSUtils.getClassAsJsClass(clazz)).append("\n");
		}
		sb.append("</script>");
		return sb.toString();
	}

	/*public static void addClassAsJS(final JspWriter out, final Class<Object> clazz) throws IOException {
		out.append(String.format("<script>\n%s</script>", JSUtils.getClassAsJsClass(clazz)));
	}*/

	@SuppressWarnings("rawtypes") // suppressing because the class will only be
									// scanned via reflection
	private static String getClassAsJsClass(final Class clazz) {
		final String template = JSUtils.getJSFunctionTemplate(clazz.getSimpleName());
		final StringBuffer sb = new StringBuffer();
		for (final Field field : clazz.getFields()) {
			try {
				final int mod = field.getModifiers();
				if (Modifier.isPublic(mod) && Modifier.isStatic(mod)) {
					if (field.getType() == int.class || field.getType() == Integer.class
							|| field.getType() == float.class || field.getType() == Float.class) {
						sb.append("  this.").append(field.getName()).append(" = ");
						sb.append(field.get(null)).append(";\n");
					} else if (field.getType() == String.class) {
						sb.append("  this.").append(field.getName()).append(" = ");
						sb.append("'").append(field.get(null)).append("';\n");
					}

				}
			} catch (final Exception e) { /* just ignore */
			}
		}
		return template.replaceAll("§", sb.toString());
	}

	private static String getJSFunctionTemplate(final String functionName) {
		return String.format("function class_%s() {\n§\n}\nvar %s = new class_%s();\n", functionName, functionName,
				functionName);
	}
}

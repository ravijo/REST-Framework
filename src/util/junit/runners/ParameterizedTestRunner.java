package util.junit.runners;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class ParameterizedTestRunner extends Suite {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface Parameter {

	int value() default 0;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Parameters {

	String name() default "{index}";
    }

    protected class TestClassRunnerForParameters extends BlockJUnit4ClassRunner {
	private final Object[] fParameters;

	private final String fName;

	protected TestClassRunnerForParameters(Class<?> type, String pattern,
		int index, Object[] parameters) throws InitializationError {
	    super(type);

	    fParameters = parameters;
	    fName = nameFor(pattern, index, parameters);
	}

	@Override
	protected Statement classBlock(RunNotifier notifier) {
	    return childrenInvoker(notifier);
	}

	@Override
	public Object createTest() throws Exception {
	    if (fieldsAreAnnotated()) {
		return createTestUsingFieldInjection();
	    } else {
		return createTestUsingConstructorInjection();
	    }
	}

	private Object createTestUsingConstructorInjection() throws Exception {
	    return getTestClass().getOnlyConstructor().newInstance(fParameters);
	}

	private Object createTestUsingFieldInjection() throws Exception {
	    List<FrameworkField> annotatedFieldsByParameter = getAnnotatedFieldsByParameter();
	    if (annotatedFieldsByParameter.size() != fParameters.length) {
		throw new Exception(
			"Wrong number of parameters and @Parameter fields."
				+ " @Parameter fields counted: "
				+ annotatedFieldsByParameter.size()
				+ ", available parameters: "
				+ fParameters.length + ".");
	    }
	    Object testClassInstance = getTestClass().getJavaClass()
		    .newInstance();
	    for (FrameworkField each : annotatedFieldsByParameter) {
		Field field = each.getField();
		Parameter annotation = field.getAnnotation(Parameter.class);
		int index = annotation.value();
		try {
		    field.set(testClassInstance, fParameters[index]);
		} catch (IllegalArgumentException iare) {
		    throw new Exception(getTestClass().getName()
			    + ": Trying to set " + field.getName()
			    + " with the value " + fParameters[index]
			    + " that is not the right type ("
			    + fParameters[index].getClass().getSimpleName()
			    + " instead of " + field.getType().getSimpleName()
			    + ").", iare);
		}
	    }
	    return testClassInstance;
	}

	@Override
	protected String getName() {
	    return fName;
	}

	@Override
	protected Annotation[] getRunnerAnnotations() {
	    return new Annotation[0];
	}

	protected String nameFor(String pattern, int index, Object[] parameters) {
	    String finalPattern = pattern.replaceAll("\\{index\\}",
		    Integer.toString(index));
	    String name = MessageFormat.format(finalPattern, parameters);
	    // return "[" + name + "]";
	    return name;
	}

	@Override
	protected String testName(FrameworkMethod method) {
	    // return method.getName() + getName();
	    return getName();
	}

	@Override
	protected void validateConstructor(List<Throwable> errors) {
	    validateOnlyOneConstructor(errors);
	    if (fieldsAreAnnotated()) {
		validateZeroArgConstructor(errors);
	    }
	}

	@Override
	protected void validateFields(List<Throwable> errors) {
	    super.validateFields(errors);
	    if (fieldsAreAnnotated()) {
		List<FrameworkField> annotatedFieldsByParameter = getAnnotatedFieldsByParameter();
		int[] usedIndices = new int[annotatedFieldsByParameter.size()];
		for (FrameworkField each : annotatedFieldsByParameter) {
		    int index = each.getField().getAnnotation(Parameter.class)
			    .value();
		    if (index < 0
			    || index > annotatedFieldsByParameter.size() - 1) {
			errors.add(new Exception("Invalid @Parameter value: "
				+ index + ". @Parameter fields counted: "
				+ annotatedFieldsByParameter.size()
				+ ". Please use an index between 0 and "
				+ (annotatedFieldsByParameter.size() - 1) + "."));
		    } else {
			usedIndices[index]++;
		    }
		}
		for (int index = 0; index < usedIndices.length; index++) {
		    int numberOfUse = usedIndices[index];
		    if (numberOfUse == 0) {
			errors.add(new Exception("@Parameter(" + index
				+ ") is never used."));
		    } else if (numberOfUse > 1) {
			errors.add(new Exception("@Parameter(" + index
				+ ") is used more than once (" + numberOfUse
				+ ")."));
		    }
		}
	    }
	}
    }

    private static final List<Runner> NO_RUNNERS = Collections
	    .<Runner> emptyList();

    private final List<Runner> fRunners;

    public ParameterizedTestRunner(Class<?> klass) throws Throwable {
	super(klass, NO_RUNNERS);
	Parameters parameters = getParametersMethod().getAnnotation(
		Parameters.class);
	fRunners = Collections.unmodifiableList(createRunnersForParameters(
		allParameters(), parameters.name()));
    }

    @SuppressWarnings("unchecked")
    private Iterable<Object> allParameters() throws Throwable {
	Object parameters = getParametersMethod().invokeExplosively(null);
	if (parameters instanceof Iterable) {
	    return (Iterable<Object>) parameters;
	} else if (parameters instanceof Object[]) {
	    return Arrays.asList((Object[]) parameters);
	} else {
	    throw parametersMethodReturnedWrongType();
	}
    }

    protected Runner createRunner(String pattern, int index, Object[] parameters)
	    throws InitializationError {
	return new TestClassRunnerForParameters(getTestClass().getJavaClass(),
		pattern, index, parameters);
    }

    private List<Runner> createRunnersForParameters(
	    Iterable<Object> allParameters, String namePattern)
	    throws Exception {
	try {
	    int i = 0;
	    List<Runner> children = new ArrayList<Runner>();
	    for (Object parametersOfSingleTest : allParameters) {
		children.add(createRunnerWithNotNormalizedParameters(
			namePattern, i++, parametersOfSingleTest));
	    }
	    return children;
	} catch (ClassCastException e) {
	    throw parametersMethodReturnedWrongType();
	}
    }

    private Runner createRunnerWithNotNormalizedParameters(String pattern,
	    int index, Object parametersOrSingleParameter)
	    throws InitializationError {
	Object[] parameters = (parametersOrSingleParameter instanceof Object[]) ? (Object[]) parametersOrSingleParameter
		: new Object[] { parametersOrSingleParameter };
	return createRunner(pattern, index, parameters);
    }

    private boolean fieldsAreAnnotated() {
	return !getAnnotatedFieldsByParameter().isEmpty();
    }

    private List<FrameworkField> getAnnotatedFieldsByParameter() {
	return getTestClass().getAnnotatedFields(Parameter.class);
    }

    @Override
    protected List<Runner> getChildren() {
	return fRunners;
    }

    private FrameworkMethod getParametersMethod() throws Exception {
	List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(
		Parameters.class);
	for (FrameworkMethod each : methods) {
	    if (each.isStatic() && each.isPublic()) {
		return each;
	    }
	}

	throw new Exception("No public static parameters method on class "
		+ getTestClass().getName());
    }

    private Exception parametersMethodReturnedWrongType() throws Exception {
	String className = getTestClass().getName();
	String methodName = getParametersMethod().getName();
	String message = MessageFormat.format(
		"{0}.{1}() must return an Iterable of arrays.", className,
		methodName);
	return new Exception(message);
    }
}
package nh.graphql.braidexample.shopservice.braid;

import graphql.execution.instrumentation.ExecutionStrategyInstrumentationContext;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationExecutionStrategyParameters;
import graphql.language.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class RootLevelFieldCounterInstrumentation extends SimpleInstrumentation {
    private static final Logger log = LoggerFactory.getLogger( RootLevelFieldCounterInstrumentation.class );
    @Override
    public ExecutionStrategyInstrumentationContext beginExecutionStrategy(InstrumentationExecutionStrategyParameters parameters) {
        if (isRootLevel(parameters)) {
            final Map<String, List<Field>> fields = parameters.getExecutionStrategyParameters().getFields();
            log.info("Number of Root-Level-Fields: {}", fields.size());
        }
        return super.beginExecutionStrategy(parameters);
    }

    private boolean isRootLevel(InstrumentationExecutionStrategyParameters parameters) {
        final int level = parameters.getExecutionStrategyParameters().getExecutionStepInfo().getPath().getLevel();

        return level == 0;
    }
}

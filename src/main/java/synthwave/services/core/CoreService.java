package synthwave.services.core;

import synthwave.dto.v1.RuleDTO;
import platform.utils.access.v1.RightManager;
import java.util.Arrays;
import spark.Request;


public abstract class CoreService {
    
    protected static String[] getExcludes(
            Request request,
            RuleDTO rule,
            String[] publicExcludes,
            String[] privateExcludes
    ) {
        return RightManager.getExcludes(
                request, 
                rule, 
                publicExcludes, 
                privateExcludes
        );
    }
    
    protected static String[] getExcludes(
            boolean isTrusted,
            RuleDTO rule,
            String[] publicExludes,
            String[] privateExcludes
    ) {
        return RightManager.getExludesByRule(
                isTrusted, 
                rule, 
                publicExludes, 
                privateExcludes
        );
    }
    
    protected static String[] getExcludes(
            Request request,
            RuleDTO rule,
            String[] globalExcludes,
            String[] publicExcludes,
            String[] privateExcludes
    ) {
        return RightManager.getExcludes(
                request, 
                rule, 
                globalExcludes, 
                publicExcludes, 
                privateExcludes
        );
    }
     
    protected static String[] getExcludes(
            RuleDTO rule,
            boolean other,
            String[] globalExcludes,
            String[] publicExcludes,
            String[] privateExcludes
    ) {
        if (rule != null) {
            var state = (other) ? rule.getMyAccess() : rule.getOtherAccess();
            var result = switch(state) {
                case "Full" -> globalExcludes;
                case "PublicAndPrivate" -> privateExcludes;
                default -> publicExcludes;
            };
            return result;
        } else {
            return publicExcludes;
        }
    }
}

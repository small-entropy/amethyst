package Services.core;

import DataTransferObjects.v1.RuleDTO;
import Utils.v1.RightManager;
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
            return switch(state) {
                case "Full" -> globalExcludes;
                case "PublicAndPrivate" -> privateExcludes;
                default -> publicExcludes;
            };
        } else {
            return publicExcludes;
        }
    }
}

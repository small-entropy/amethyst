package Services.base;

import DataTransferObjects.v1.RuleDTO;
import Utils.v1.RightManager;
import spark.Request;

/**
 * Base class for 
 * @author small-entropy
 */
public abstract class BaseService <R> {
    R repository;
    
    private String[] publicExcludes = new String[] {};
    private String[] privateExcludes = new String[] {};
    private String[] globalExcludes = new String[] {};
    
    public BaseService(R repository) {
        this.repository = repository;
    }
    
    public BaseService(
            R repository,
            String[] globalExcludes,
            String[] publicExcludes,
            String[] privateExcludes
        ) {
        this.repository = repository;
        this.publicExcludes = publicExcludes;
        this.privateExcludes = privateExcludes;
        this.globalExcludes = globalExcludes;
    }

    public R getRepository() {
        return repository;
    }

    public void setRepository(R repository) {
        this.repository = repository;
    }

    public String[] getPublicExcludes() {
        return publicExcludes;
    }

    public void setPublicExcludes(String[] publicExcludes) {
        this.publicExcludes = publicExcludes;
    }

    public String[] getPrivateExcludes() {
        return privateExcludes;
    }

    public void setPrivateExcludes(String[] privateExcludes) {
        this.privateExcludes = privateExcludes;
    }

    public String[] getGlobalExcludes() {
        return globalExcludes;
    }
    
    public void setGlobalExcludes(String[] globalExcludes) {
        this.globalExcludes = globalExcludes;
    }
    
    protected String[] getExcludes(
            Request request,
            RuleDTO rule
    ) {
        return RightManager.getExcludes(
                request, 
                rule,
                globalExcludes,
                publicExcludes, 
                privateExcludes
        );
    }
    
    protected String[] getExcludes(
            boolean isTrusted,
            RuleDTO rule
    ) {
        return RightManager.getExludesByRule(
                isTrusted, 
                rule,
                globalExcludes,
                publicExcludes, 
                privateExcludes
        );
    }
     
    protected String[] getExcludes(
            RuleDTO rule,
            boolean other
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

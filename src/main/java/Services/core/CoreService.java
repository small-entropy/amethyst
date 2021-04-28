package Services.core;

import DataTransferObjects.RuleDTO;

public abstract class CoreService {
    /**
     * Method for get find options argument by rule my documents access state
     * @param rule user rule
     * @return arguments for find options
     */
    protected static String[] getMyFindOptionsArgs(RuleDTO rule) {
        return (rule != null)
                ? switch (rule.getMyAccess()) {
            case "Full" -> CoreUserService.ALL_ALLOWED;
            case "PublicAndPrivate" -> CoreUserService.PUBLIC_AND_PRIVATE_ALLOWED;
            default -> CoreUserService.PUBLIC_ALLOWED;
        }
                : CoreUserService.PUBLIC_ALLOWED;
    }

    /**
     * Method for get find options argument by rule other documents access state
     * @param rule user rule
     * @return arguments for find options
     */
    protected static String[] getOtherFindOptionsArgs(RuleDTO rule) {
        return (rule != null)
                ? switch (rule.getOtherAccess()) {
            case "Full" -> CoreUserService.ALL_ALLOWED;
            case "PublicAndPrivate" -> CoreUserService.PUBLIC_AND_PRIVATE_ALLOWED;
            default -> CoreUserService.PUBLIC_ALLOWED;
        }
                : CoreUserService.PUBLIC_ALLOWED;
    }
}

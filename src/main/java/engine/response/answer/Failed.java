package engine.response.answer;

import core.response.answers.BaseAnswer;
import core.response.answers.BaseAnswer;
import core.response.answers.Meta;

/**
 * Class for error response
 */
public class Failed extends BaseAnswer {
    /**
     * Constructor for error response by message & meta
     * @param message value of message
     * @param meta meta object
     */
    public Failed(String message, Meta meta) {
        super("error", message, meta);
    }

    /**
     * Constructor for error response by only message
     * @param message value of message
     */
    public Failed(String message) {
        super("error", message);
    }
}

package engine.response.answer;

import core.response.answers.Meta;
import engine.response.answer.Answer;

/**
 * Class for create success response object (always have a "success" status)
 * @param <D> type of data property
 */
public class Success<D> extends Answer<D> {
    /**
     * Constructor for response object with message, data & meta property
     * @param message response message
     * @param data data for response
     * @param meta meta for response
     */
    public Success(String message, D data, Meta meta) {
        super("success", message, data, meta);
    }

    /**
     * Constructor for response object with message & data
     * @param message response message
     * @param data response data
     */
    public Success(String message, D data) {
        super("success", message, data);
    }
}

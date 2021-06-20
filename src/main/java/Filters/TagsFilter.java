/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Filters;

/**
 *
 * @author small-entropy
 */
public class TagsFilter extends Filter {
    private String value;

    public TagsFilter() {
        super();
    }

    public TagsFilter(String[] exludes) {
        super(exludes);
    }

    public TagsFilter(int skip, int limit, String[] excludes) {
        super(skip, limit, excludes);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

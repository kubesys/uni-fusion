package com.github.restapi.models;

import java.util.List;

/**
 * @author Oldhand
 **/

public class FetchResult {
    public int size;
    public List entery;

    public FetchResult(int size, List entery) {
        this.size = size;
        this.entery = entery;
    }

}

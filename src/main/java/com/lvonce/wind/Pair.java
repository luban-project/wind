package com.lvonce.wind;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<T, K> {
    private T first;
    private K second;
    public static<F, G> Pair<F, G> of(F first, G second) {
        return new Pair<>(first, second);
    }
}

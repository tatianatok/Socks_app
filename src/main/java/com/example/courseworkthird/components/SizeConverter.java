package com.example.courseworkthird.components;

import com.example.courseworkthird.model.Size;
import org.springframework.core.convert.converter.Converter;


public class SizeConverter implements Converter<String, Size> {

    @Override
    public Size convert(String source) {
        return Size.convertSize(Integer.parseInt(source));
    }
}
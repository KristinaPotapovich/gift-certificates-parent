package com.epam.esm.util;

import com.epam.esm.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class TagValidation {
    private static String TAG_NAME_REGEX = "[a-zA-Zа-яА-Я]+";
    private static int MAX_SIZE_NAME = 50;
    public void isRightTagName(String name){
       if (!name.matches(TAG_NAME_REGEX) || name.length()> MAX_SIZE_NAME){
           throw new ValidationException("Tag with wrong name");
       }
    }
   public void isEmptyName(String name){
        if (name == null || name.trim().isEmpty()){
            throw new ValidationException("Tag is empty");
        }
   }

}

/*
Copyright 2009-2010 Igor Polevoy 

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/


package activejdbc.validation;

import activejdbc.*;
import javalite.common.Convert;


import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

public class NumericValidator extends ValidatorAdapter {
    private String attribute;

    private Double min = null;
    private Double max = null;
    private boolean allowNull = false, onlyInteger = false, convertNullIfEmpty = false;


    public NumericValidator(String attribute) {
        this.attribute = attribute;
        setMessage("value is not a number");
    }


    public void validate(Model m) {
        Object value = m.get(attribute);

        if(convertNullIfEmpty && "".equals(value)){
            m.set(attribute, null);
            value = null;
        }

        if(value == null && allowNull){
            return;
        }

        //this is to check just numericality
        if (value != null) {
            try {
                ParsePosition pp = new ParsePosition(0);
                String input = value.toString();
                NumberFormat.getInstance().parse(input, pp);
                if(pp.getIndex() != (input.length()))
                    throw new RuntimeException("");
            } catch (Exception e) {
                m.addValidator(attribute, this);
            }
        } else {
                m.addValidator(attribute, this);
        }

        if(min != null){
            validateMin(Convert.toDouble(value), m);
        }

        if(max != null){
            validateMax(Convert.toDouble(value), m);
        }

        if(onlyInteger){
            validateIntegerOnly(value, m);
        }
    }

    private void validateMin(Double value, Model m){
        if(value <= min){
            m.addValidator(attribute, this);
        }
    }

    private void validateIntegerOnly(Object value, Model m){        
        try{
            Integer.valueOf(value.toString());
        }
        catch(Exception e){
            m.addValidator(attribute, this);
        }
    }



    private void validateMax(Double value, Model m){
        if(value >= max){
            m.addValidator(attribute, this);
        }
    }


    public void setMin(Double min){
        this.min = min;
    }

    public void setMax(Double max){
        this.max = max;
    }
    public void setAllowNull(Boolean allowNull){
        this.allowNull = allowNull;
    }

    public void setOnlyInteger(boolean onlyInteger){
        this.onlyInteger = onlyInteger;
    }

    public void convertNullIfEmpty(boolean convertNullIfEmpty) {
        this.convertNullIfEmpty = convertNullIfEmpty;
    }



    public static void main(String[] av) throws ParseException {

        String input = "11 ss";
        ParsePosition pp = new ParsePosition(0);
        NumberFormat.getInstance().parse(input, pp);
        
        if(pp.getIndex() != (input.length() - 1))
            throw new RuntimeException("failed to parse");

    }

}
package com.yenusoft.revelation;

/**
 * Created by SofKaints on 2017. 7. 28..
 */

public class enums
{
    public enum lang
    {
        kor(0), eng(1);
        private int value;
        lang(int _value) {
            value = _value;
        }
        public int getValue()
        {
            return value;
        }
    }

    public enum startType
    {
        order(0), random(1), all_random(2);
        private int value;
        startType(int _value) {
            value = _value;
        }
        public int getValue()
        {
            return value;
        }
    }
}

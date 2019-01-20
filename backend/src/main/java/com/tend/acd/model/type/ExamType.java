package com.tend.acd.model.type;

/**
 * Created by Cyokin
 * on 4/14/2016.
 */
public enum ExamType {
    @EnumValue("填空题")
    blankFilling,

    @EnumValue("多选题")
    multipleSelection,

    @EnumValue("是非题")
    trueFalse,

    @EnumValue("单选题")
    multipleChoice,

    @EnumValue("问答题")
    questionsnswers,

    @EnumValue("阅读理解题")
    readingComprehension
}

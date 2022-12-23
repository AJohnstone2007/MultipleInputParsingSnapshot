package com.javafx.experiments.importers.maya.types;
import com.javafx.experiments.importers.maya.MEnv;
import com.javafx.experiments.importers.maya.values.MData;
import com.javafx.experiments.importers.maya.values.impl.MStringImpl;
public class MStringType extends MDataType {
public static final String NAME = "string";
public MStringType(MEnv env) {
super(env, NAME);
}
public MData createData() {
return new MStringImpl(this);
}
}

copy manifest.tmp ..\classes
cd ..\classes
jar -cfm AssessmentBeans.jar manifest.tmp org\cougaar\lib\uiframework\ui
del ..\scripts\AssessmentBeans.jar
move AssessmentBeans.jar ..\scripts
cd ..\scripts

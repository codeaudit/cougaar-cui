#!/bin/csh

setenv SP ./src

setenv OP ./classes

# *** alp-demo **** set LIB e:/DELTAInst/lib
# *** TIC **** set LIB ./lib
# the following is for when the build.bat is in uiframework directory
setenv LIB ../../lib

setenv CP
setenv CP ${CP}:${LIB}/xerces.jar
setenv CP ${CP}:${LIB}/vgj.jar
setenv CP ${CP}:${LIB}/aggagent.jar
setenv CP ${CP}:${LIB}/core.jar
setenv CP ${CP}:${LIB}/glm.jar
setenv CP ${CP}:${LIB}/planserver.jar
setenv CP ${CP}:${LIB}/xml4j_2_0_11.jar
setenv CP ${CP}:${LIB}/j2ee.jar
setenv CP ${CP}:${LIB}/cuimap.jar

setenv SRCR src/org/cougaar/lib/uiframework
setenv SRBJ src/mil/darpa/log/alpine/blackjack

setenv JF1
setenv JF2
setenv JF3

setenv JF1 "${JF1} ${SRCR}/query/*.java"
setenv JF1 "${JF1} ${SRCR}/query/generic/*.java"
setenv JF1 "${JF1} ${SRCR}/query/test/*.java"
setenv JF1 "${JF1} ${SRCR}/transducer/*.java"
setenv JF1 "${JF1} ${SRCR}/transducer/configs/*.java"
setenv JF1 "${JF1} ${SRCR}/transducer/elements/*.java"
setenv JF1 "${JF1} ${SRCR}/transducer/dbsupport/*.java"
setenv JF1 "${JF1} ${SRCR}/ui/orglocation/data/*.java"
setenv JF1 "${JF1} ${SRCR}/ui/orglocation/plugin/*.java"
setenv JF1 "${JF1} ${SRCR}/ui/orglocation/psp/*.java"
setenv JF1 "${JF1} ${SRCR}/ui/orglocation/psp/xmlservice/*.java"
setenv JF1 "${JF1} ${SRCR}/ui/orgui/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/VGJ/algorithm/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/VGJ/algorithm/tree/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/VGJ/algorithm/cgd/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/VGJ/algorithm/cartegw/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/VGJ/algorithm/shawn/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/VGJ/examplealg/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/VGJ/gui/*.java "
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/VGJ/graph/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/VGJ/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/*.java"
# setenv JF2 "${JF2} ${SRCR}/ui/ohv/util/*.java"
setenv JF2 "${JF2} ${SRCR}/ui/components/*.java"
setenv JF2 "${JF2} ${SRCR}/ui/components/graph/*.java"
setenv JF2 "${JF2} ${SRCR}/ui/components/mthumbslider/*.java"
setenv JF3 "${JF3} ${SRCR}/ui/map/app/*.java"
setenv JF3 "${JF3} ${SRCR}/ui/map/layer/*.java"
setenv JF3 "${JF3} ${SRCR}/ui/map/util/*.java"
setenv JF3 "${JF3} ${SRCR}/ui/models/*.java"
setenv JF3 "${JF3} ${SRCR}/ui/themes/*.java"
setenv JF3 "${JF3} ${SRCR}/ui/util/*.java"

setenv JF3 "${JF3} ${SRBJ}/assessui/middletier/*.java"
setenv JF3 "${JF3} ${SRBJ}/assessui/webtier/*.java"
setenv JF3 "${JF3} ${SRBJ}/assessui/client/*.java"
setenv JF3 "${JF3} ${SRBJ}/assessui/util/*.java"
setenv JF3 "${JF3} ${SRBJ}/assessui/society/*.java"

javac -classpath ${CP} -sourcepath ${SP} -d ${OP} ${JF1}
javac -classpath ${CP} -sourcepath ${SP} -d ${OP} ${JF2}
javac -classpath ${CP} -sourcepath ${SP} -d ${OP} ${JF3}

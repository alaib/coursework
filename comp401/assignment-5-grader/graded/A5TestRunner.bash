#!/bin/bash

baseDir="/playpen/comp401/A5/graded"
for file in *; do
    if [ ! -d "$file" ];then
        continue
    fi

    echo -e "============================================================================================";
    echo -n "Executing test for user = $file (s = skip, q = quit, any key = continue) = "
    read input
    if [ "$input" == "s" ]; then
        continue;
    elif [ "$input" == "q" ]; then
        break;
    fi

    #Extract userName
    userName=${file//.\//};

    submitDir="$file/Submission attachment(s)/";
    if [ ! -d "$submitDir" ]; then
        echo "$userName does not have submission directory"
        continue;
    fi

    #Change directory to Submission
    cd "$submitDir"
    #Create a5 directory
    mkdir -p a5
    #Copy src files to a5
    cp ../../../src/* a5/

    #Copy ChessPiece.java in previous directory to a5, if not present, output their name
    chessPieceFile="ChessPiece.java"
    if [ ! -f $chessPieceFile ]; then
        echo -e "$userName - NO SUBMISSION";
        echo -e "============================================================================================\n";
        cd $baseDir;
        continue;
    fi

    cp ChessPiece.java a5/
    echo -e "Copying ChessPiece.java to a5 - OK"

    #Compile files
    compileCmd="javac -cp /usr/share/java/junit4.jar a5/Chess*.java a5/A5Tester.java"
    $compileCmd

    #Check exit status
    if [ $? -ne 0 ]; then
        echo "COMPILATION FAILED"
        continue;
    else
        echo "COMPILATION SUCCEEDED"
    fi

    #Run the tests and get status
    echo "EXECUTING TESTS"
    result=`java -cp /usr/share/java/junit4.jar:. org.junit.runner.JUnitCore a5.A5Tester | grep -i 'tests'`

    #Extract status from result
    totalTests=25
    if [[ $result =~ .*Failures.*  ]]; then
        failure=`echo $result | sed 's/.*Failures: //g'`
        success=$(expr $totalTests - $failure)
    else
        failure=0
        success=25
    fi

    echo "Total Tests = $totalTests, Success = $success, Failure = $failure, Points = $success"

    echo -n "Should I open ChessPiece.java for grading style points (y | n) = "
    read input
    if [ "$input" == "y" ]; then
        gedit ChessPiece.java
    fi
    
    echo -e "============================================================================================\n";
    cd $baseDir;
done

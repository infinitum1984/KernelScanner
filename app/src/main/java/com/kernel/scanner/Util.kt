package com.kernel.scanner

fun findCodeInString(input:String):String{
    var processedStr=""
    for (ch in input.toCharArray()){
        if (ch.isLetterOrDigit()){
            processedStr+=ch
        }
    }
    if (processedStr.length>=9){
        return processedStr.substring(0,9)
    }else{
       return ""
    }
}
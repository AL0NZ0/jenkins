#!/usr/bin/env groovy
@Library('jenkinslib@dev1') _


def test() {
    def mytools = new org.devops.tools()
    mytools.do_silences("enable")
    mytools.do_silences("disable")
}

def do_blank() {
    echo "about:blank"
}



node("master") {
    stage('Checkout')   { do_blank() }
    stage('Build')      { do_blank() }
    stage('Test')       { do_blank() }
}
stage('Install') {
    node("master")  { do_blank() }
}
stage('Release') {
    node("master")  { test() }
}

#!groovy
@Library('jenkinslibrary@master') _
def mytools = new org.devops.tools()
def build = new org.devops.build()
String BuildShell = "${env.BuildShell}"
String BuildType = "${env.BuildType}"

String SrcUrl = "${env.SrcUrl}"
String BranchName = $"{env.BranchName}"

pipeline {
    agent {
        node {label "master"}
    }
    options {
        timestamps()
        timeout(time: 1, unit: "HOURS")
    }
    
    stages {
        stage("Build") {
            steps{
                script{
                    build.Build(buildType,buildShell)
                }
            }
        }      
    }
}

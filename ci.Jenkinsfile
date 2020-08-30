#!groovy
@Library('jenkinslibrary@master') _
def mytools = new org.devops.tools()
def build = new org.devops.build()
String BuildShell = "${env.BuildShell}"
String BuildType = "${env.BuildType}"

String SrcUrl = "${env.SrcUrl}"
String BranchName = "${env.BranchName}"

pipeline {
    agent {
        node {label "master"}
    }
    options {
        timestamps()
        timeout(time: 1, unit: "HOURS")
    }
    
    stages {
         stage("CheckOut"){
            steps{
                script{
                      println("${BranchName}")
/*                    checkout([$class: 'GitSCM', branches: [[name: "${BranchName}"]], 
                                      doGenerateSubmoduleConfigurations: false, 
                                      extensions: [], 
                                      submoduleCfg: [], 
                                      userRemoteConfigs: [[credentialsId: 'global-git-credential', url: "${SrcUrl}"]]])*/
                      checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'global-git-credential', url: 'https://github.com/pop9009/simple-java-maven-app.git']]])
                }
            }
        }
        stage("Build") {
            steps{
                script{
                    build.Build(buildType,buildShell)
                }
            }
        }      
    }
}

#!groovy
@Library('jenkinslibrary@master') _
def mytools = new org.devops.tools()
def build = new org.devops.build()
def gitlab = new org.devops.gitlab()
def sonar = new org.devops.sonarqube()

def runOpts
    
String BuildShell = "${env.BuildShell}"
String BuildType = "${env.BuildType}"
String BranchName = "${env.branchName}"

String SrcUrl = "${env.SrcUrl}"

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
                    mytools.PrintMes("获取代码", 'blue')
                    if("${runOpts}" == "GitlabPush"){
                        env.BranchName = branch-"refs/heads/"
                        gitlab.ChangeCommitStatus(projectId,commitSha,"running")
                    }
                    println("${BranchName}")
                    checkout([$class: 'GitSCM', branches: [[name: "${BranchName}"]], 
                                      doGenerateSubmoduleConfigurations: false, 
                                      extensions: [], 
                                      submoduleCfg: [], 
                                      userRemoteConfigs: [[credentialsId: 'global-git-credential', url: "${SrcUrl}"]]])
//                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'global-git-credential', url: 'https://github.com/pop9009/simple-java-maven-app.git']]])
                }
            }
        }
        stage("Build") {
            steps{
                script{
                    mytools.PrintMes("打包", 'red')
                    build.Build(buildType,buildShell)
                }
            }
        } 
        stage("QA"){
            steps{
                script{
                    mytools.PrintMes("代码扫描", 'green')
                    sonar.SonarScan("${JOB_NAME}","${JOB_NAME}","src")
                }
            }
        }
    }
    post {
        always{
            script{
                println("always")
            }
        }
        
        success{
            script{
                println("success")
                if("${runOpts}" == "GitlabPush"){
                    gitlab.ChangeCommitStatus(projectId,commitSha,"success")  
                }              
            }    
        }
        failure{
            script{
                println("failure")
                if("${runOpts}" == "GitlabPush"){
                    gitlab.ChangeCommitStatus(projectId,commitSha,"failed")   
                }            
            }
        }
        
        aborted{
            script{
                println("aborted")
                if("${runOpts}" == "GitlabPush"){
                    gitlab.ChangeCommitStatus(projectId,commitSha,"canceled")
                }
            }        
        }    
    }
}

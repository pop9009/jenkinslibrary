#!groovy
@Library('jenkinslibrary@master') _
def mytools = new org.devops.tools()
def build = new org.devops.build()
def gitlab = new org.devops.gitlab()

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
                    mytools.PrintMes("获取代码", 'blue')
                    if("${runOpts}" == "GitlabPush"){
                        BranchName = branch-"refs/heads/"
                        gitlab.ChangeCommitStatus(projectId,commitSha,"running")
                    }
                    println("${branch}")
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
                gitlab.ChangeCommitStatus(projectId,commitSha,"success")                
            }    
        }
        failure{
            script{
                println("failure")
                gitlab.ChangeCommitStatus(projectId,commitSha,"failed")               
            }
        }
        
        aborted{
            script{
                println("aborted")
                gitlab.ChangeCommitStatus(projectId,commitSha,"canceled")
            }        
        }    
    }
}

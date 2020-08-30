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
        stage("CheckOut"){
            steps{
                script{
                    checkout([$class: 'GitSCM', branches: [[name: "${BranchName}"]], 
                                      doGenerateSubmoduleConfigurations: false, 
                                      extensions: [], 
                                      submoduleCfg: [], 
                                      userRemoteConfigs: [[credentialsId: 'global-git-credential', url: "${SrcUrl}"]]])
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
    post{
        always {
            script{
                println("Always")
                println("${params.DEPLOY_ENV}")
            }
        }
        success {
            script{
                currentBuild.description = "\n构筑成功"
            }
        }
        failure {
            script{
                currentBuild.description = "\n构筑失败"
            }
        }
        aborted {
            script{
                currentBuild.description = "\n构筑取消"
            }
        }
    }
}

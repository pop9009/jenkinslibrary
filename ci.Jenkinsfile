
#!groovy
@Library('jenkinslibrary@master') _
def mytools = new org.devops.tools()
def build = new org.devops.build()
String BuildShell = "${env.BuildShell}"
String BuildType = "${env.BuildType}"
hello()

pipeline {
    agent {
        node {label "master"}
    }
    parameters { string(name: 'DEPLOY_ENV', defaultValue: 'dev', description: '') }
    options {
        timestamps()
        timeout(time: 1, unit: "HOURS")
    }
    
    stages {
        stage('GetCode') {
            when {
                environment name: 'test', value: 'abcd'
            }
            options {
                timeout(time: 10, unit: 'MINUTES') 
            }
            steps {
                script{
                    println("获取代码")
                    mytools.PrintMes("获取代码", 'red')
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
        stage('CodeScan') {
            options {
                timeout(time: 30, unit: 'MINUTES') 
            }
            steps {
                script{
                    println("代码扫描")
                    mytools.PrintMes("代码扫描", 'blue')
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

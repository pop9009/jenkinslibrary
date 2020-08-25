#!groovy
@Library('jenkinslibrary') _
def mytools = new org.devops.tools()
String BuildShell = "${env.BuildShell}"

hello()

pipeline {
    agent {
        node {label "master"}
    }
    parameters { string(name: 'DEPLOY_ENV', defaultValue: 'dev', description: '') }
    tools {
        maven 'm2' 
    }
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
        stage('Build') {
            options {
                timeout(time: 20, unit: 'MINUTES') 
            }
            steps {
                script{
                    println("应用打包")
                    mytools.PrintMes("应用打包", 'green')
                    java = tool "JavaHome"
                    sh "${java}/bin/java -version"
//                    println(java)
                    mvnHome = tool M2
//                    println(mvnHome)
                    sh "${mvnHome}/bin/mvn --version"
                }
                sh 'mvn --version'
            }
        }
        stage("GradleBuild"){
            steps{
                script{
                    gradleHome = tool "GRADLE"
                    sh "${gradleHome}/bin/gradle ${BuildShell}"
                }
            }
        }
        stage("NpmBuild"){
            steps{
                script{
                    npmHome = tool "NPM"
                    sh "export NODE_HOME=${npmHome} && export PATH=\$NODE_HOME/bin:\$PATH && ${npmHome}/bin/npm ${BuildShell}"
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
                    mytools.PrintMes("This is my lib!", 'red')
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

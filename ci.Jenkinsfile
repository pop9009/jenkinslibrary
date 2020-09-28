#!groovy
@Library('jenkinslibrary@master') _
def mytools = new org.devops.tools()
def build = new org.devops.build()
def gitlab = new org.devops.gitlab()
def sonar = new org.devops.sonarqube()
def sonarapi = new org.devops.sonarapi()
def nexus = new org.devops.nexus()
def runOpts = " "
    
String BuildShell = "${env.BuildShell}"
String BuildType = "${env.BuildType}"
String BranchName = "${env.branchName}"
String SrcUrl = "${env.SrcUrl}"

if("${runOpts}" == "GitlabPush"){
    BranchName = branch-"refs/heads/"
    gitlab.ChangeCommitStatus(projectId,commitSha,"running")
    env.runOpts = "GitlabPush"
}

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
                    mytools.PrintMes("打包", 'blue')
                    build.Build(buildType,buildShell)
                    nexus.main("Maven")
                    /*def jarName = sh returnStdout:true, script:"cd target;ls *.jar"
                    jarName = jarName - "\n"
                    def pom = readMavenPom file: "pom.xml"
                    pomGroupId = "${pom.groupId}"
                    pomArtifact = "${pom.artifactId}"
                    pomVersion = "${pom.version}"
                    pomPackaging = "${pom.packaging}"
                    println("${pomGroupId}-${pomArtifact}-${pomVersion}-${pomPackaging}")
                    
                    def mvnHome = tool "M2"
                    sh  """ 
                        cd target/
                        ${mvnHome}/bin/mvn deploy:deploy-file -Dmaven.test.skip=true  \
                            -Dfile=${jarName} -DgroupId=${pomGroupId} \
                            -DartifactId=${pomArtifact} -Dversion=${pomVersion}  \
                            -Dpackaging=${pomPackaging} -DrepositoryId=maven-snapshots \
                            -Durl=http://172.100.25.65:8081/repository/maven-snapshots 
                        """
                    def filepath = "target/${jarName}"
                    def repoName = "maven-snapshots"
                    nexusArtifactUploader artifacts: [[artifactId: "${pomArtifact}",
                                                       classifier: '',
                                                       file: "${filepath}",
                                                       type: "${pomPackaging}"]],
                                                       credentialsId: 'nexus-credential',
                                                       groupId: "${pomGroupId}",
                                                       nexusUrl: '172.100.25.65:8081',
                                                       nexusVersion: 'nexus3',
                                                       protocol: 'http',
                                                       repository: "${repoName}",
                                                       version: "${pomVersion}"*/
                }
            }
        } 
        stage("QA"){
            steps{
                script{
                    mytools.PrintMes("搜索项目", 'green')
                    result=sonarapi.SearchProject("${JOB_NAME}")
                    if(result == "false"){
                        println("项目不存在！创建项目")
                        sonarapi.CreateProject("${JOB_NAME}")
                    }else{
                        println("项目已存在！")
                    }
                    mytools.PrintMes("配置项目质量规则",'green')
                    //qpName="${JOB_NAME}".split("-")[0]
                    qpName='Sonar%20way'
                    println(qpName)
                    sonarapi.ConfigQualityProfiles("${JOB_NAME}","java",qpName)
                    mytools.PrintMes("配置质量阈",'green')
                    sonarapi.ConfigQualityGates("${JOB_NAME}",qpName)
                    mytools.PrintMes("代码扫描", 'green')
                    sonar.SonarScan("${JOB_NAME}","${JOB_NAME}","src")
                    mytools.PrintMes("获取扫描结果", 'green')
                    sleep 10
                    result = sonarapi.GetProjectStatus("${JOB_NAME}")
                    if(result.toString() == "ERROR"){
                        error " 代码质量阈错误！请及时修复！" 
                    }else{
                        println(result)
                    }
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
                mytools.PrintMes("构建结束", 'green')
                println("success")
                if("${env.runOpts}" == "GitlabPush"){
 //                  println("runOpts: ${env.runOpts}")
                   gitlab.ChangeCommitStatus(projectId,commitSha,"success")  
              }              
            }    
        }
        failure{
            script{
                println("failure")
                if("${env.runOpts}" == "GitlabPush"){
                    gitlab.ChangeCommitStatus(projectId,commitSha,"failed")   
                }            
            }
        }
        
        aborted{
            script{
                println("aborted")
                if("${env.runOpts}" == "GitlabPush"){
                    gitlab.ChangeCommitStatus(projectId,commitSha,"canceled")
                }
            }        
        }    
    }
}

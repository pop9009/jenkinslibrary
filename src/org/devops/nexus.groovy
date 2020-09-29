package org.devops

def GetGav(){
    def jarName = sh returnStdout:true, script:"cd target;ls *.jar"
    env.jarName = jarName - "\n"
    def pom = readMavenPom file: "pom.xml"
    env.pomGroupId = "${pom.groupId}"
    env.pomArtifact = "${pom.artifactId}"
    env.pomVersion = "${pom.version}"
    env.pomPackaging = "${pom.packaging}"
    println("${pomGroupId}-${pomArtifact}-${pomVersion}-${pomPackaging}")
}

def MavenUpload(){
    def mvnHome = tool "M2"
    sh  """ 
        cd target/
        ${mvnHome}/bin/mvn deploy:deploy-file -Dmaven.test.skip=true  \
        -Dfile=${jarName} -DgroupId=${pomGroupId} \
        -DartifactId=${pomArtifact} -Dversion=${pomVersion}  \
        -Dpackaging=${pomPackaging} -DrepositoryId=maven-snapshots \
        -Durl=http://172.100.25.65:8081/repository/maven-snapshots 
         """
}

def NexusUpload(){
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
                                       version: "${pomVersion}"
}

def ArtifactUpdate(updateType,artifactUrl){
    if ("${updateType}" == "snapshot -> release"){
        println("snapshot -> release")

        //下载原始制品
        sh "  rm -fr updates && mkdir updates && cd updates && wget ${artifactUrl} && ls -l "

        //获取artifactID 
        
        artifactUrl = artifactUrl - "http://172.100.25.65:8081/repository/maven-snapshots/"
        artifactUrl = artifactUrl.split("/").toList()
        
        println(artifactUrl.size())
        env.jarName = artifactUrl[-1] 
        env.pomVersion = artifactUrl[-2].replace("SNAPSHOT","RELEASE")
        env.pomArtifact = artifactUrl[-3]
        pomPackaging = artifactUrl[-1]
        pomPackaging = pomPackaging.split("\\.").toList()[-1]
        env.pomPackaging = pomPackaging[-1]
        env.pomGroupId = artifactUrl[0..-4].join(".")
        println("${pomGroupId}##${pomArtifact}##${pomVersion}##${pomPackaging}")
        env.newJarName = "${pomArtifact}-${pomVersion}.${pomPackaging}"
        
        //更改名称
        sh " cd updates && mv ${jarName} ${newJarName} "
        println("显示当前路径")
        sh "pwd"
        println("显示jar包")
        sh "ls"
        
        //上传制品
        env.repoName = "maven-releases"
        env.filePath = "updates/${newJarName}"
        NexusUpload()
    }
}

def main(UploadType){
   GetGav()
   if("${UploadType}" == "Maven"){
      MavenUpload()
   }else if ("${UploadType}" == "Nexus"){
      env.filepath = "target/${jarName}"
      env.repoName = "maven-snapshots"
      NexusUpload()
   }
}

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
                                       version: "${pomVersion}"
}
def main(UploadType){
   GetGav()
   if("${UploadType}" == "Maven"){
      MavenUpload()
   }else if("${UploadType}" == "Nexus"){
      NexusUpload()
   }
｝

#!groovy
@Library('jenkinslibrary@master') _

def nexus = new org.devops.nexus()
def nexusapi = new org.devops.nexusapi()

pipeline{
  agent{
    node {label "master"}
    }
  stages{
    stage("UpdateArtifact"){
      steps{
        script{
          //更改制品
          //nexus.ArtifactUpdate(updateType,artifactUrl)
          
          nexusapi.GetRepoComponents("maven-public")
        }
      }
    }
  }
}

package org.devops

def build(buildType,buildShell){
  buildTools=["mvn":"M2", "gradle":"GRADLE", "npm":"NPM"]
  buildHome = tool buildTools[buildType]
  println(buildHome地址 "${buildHome}")
  if ("${buildType}" = "npm"){
    sh "export NODE_HOME=${buildHome} && export PATH=\$NODE_HOME/bin:\$PATH && ${buildHome}/bin/npm ${BuildShell}"
  }
  sh "${buildHome}/bin/${buildType} ${buildShell}"
}

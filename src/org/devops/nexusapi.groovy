package org.devops

//封装HTTP请求
def HttpReq(reqType,reqUrl,reqBody){
    def sonarServer = "http://172.100.25.65:8081/service/rest"
    result = httpRequest authentication: 'nexus-credential',
            httpMode: reqType, 
            contentType: "APPLICATION_JSON",
            consoleLogResponseBody: true,
            ignoreSslErrors: true, 
            requestBody: reqBody,
            url: "${sonarServer}/${reqUrl}"
            //quiet: true
    return result
}

//获取仓库中所有组件

def GetRepoComponents(repoName){
    apiUrl = "/v1/components?repository=${repoName}"
    response = HttpReq("GET",apiUrl,'')
    response = readJSON text: """${response.content}"""
    println(response["items"].size())
    return response["items"]
}

//获取仓库中组件ID
def GetComponentsId(repoName,groupId,artifactId,version){
    result = GetRepoComponents(repoName)
    for(component in result){
        if(component["group"] == groupId && component["name"] == artifactId && component["version"] == version){
            componentId = component["id"]
//            println("获取制品ID")
//            println(componentId)
            return componentId
        }
    }    
}

def GetSingleComponent(repoName,groupId,artifactId,version){
    componentId = GetComponentsId(repoName,groupId,artifactId,version)
    apiUrl =  "/v1/components/${componentId}"
    response = HttpReq("GET",apiUrl,'')
    response = readJSON text: """${response.content}"""
    println("组件下载地址")
    println(response["assets"]["downloadUrl"][0])
}

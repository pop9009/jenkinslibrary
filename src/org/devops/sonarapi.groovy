package org.devops

//封装HTTP请求
def HttpReq(reqType,reqUrl,reqBody){
    def sonarServer = "http://172.100.25.65:9000/api"
    result = httpRequest authentication: 'sonar-admin-user',
            httpMode: reqType, 
            contentType: "APPLICATION_JSON",
            consoleLogResponseBody: true,
            ignoreSslErrors: true, 
            requestBody: reqBody,
            url: "${sonarServer}/${reqUrl}"
                //quiet: true
    return result
}

//获取Sonar质量阈状态
def GetProjectStatus(projectName){
    apiUrl = "project_branches/list?project=${projectName}"
    response = HttpReq("GET",apiUrl,'')
    response = readJSON text: """${response.content}"""
    println(response)
    result = response["branches"][0]["status"]["qualityGateStatus"]
    return result
}

package org.devops

def do_silences(status) { // 静音由构建产生的告警
    if (status == "enable") {
        all = sh(returnStdout: true, script: "cat /etc/zsh/ps-command.sh | grep \"PS1\" | awk -F '[@\$]' '{print \$5}' | uniq | tr -d \"\\n\"")
        startat = sh(returnStdout: true, script: "date -u \"+%Y-%m-%dT%H:%M:%S.000Z\" | uniq | tr -d \"\\n\"")
        endat =  sh(returnStdout: true, script: "date -u -d \"1 hour\" \"+%Y-%m-%dT%H:%M:%S.000Z\" | uniq | tr -d \"\\n\"")
        env = all.split('-')[0]
        if (env == "us") {
            url = "10.10.10.10"
        } else {
            url = "10.20.20.20"
        }

        body = """
            {
                "matchers": [
                    {
                        "name": "all",
                        "value": "${all}",
                        "isRegex": false
                    },
                    {
                        "name": "job",
                        "value": "http",
                        "isRegex": false
                    }
                ],
                "startsAt": "${startat}",
                "endsAt": "${endat}",
                "createdBy": "it_admin",
                "comment": "silences_api"
            }
        """

        def responsePost = httpRequest contentType: 'APPLICATION_JSON',
            consoleLogResponseBody: false ,
            httpMode: "POST",
            requestBody: body,
            url: "http://${url}:9093/api/v2/silences"

        alertid = responsePost.content
        silenceid = sh(returnStdout: true, script: "echo \"${alertid}\" | awk -F '[:}]' '{print \$2}'")
    }
    if (status == "disable") {
        def responseDelete = httpRequest contentType: 'APPLICATION_JSON',
            httpMode: "DELETE",
            url: "http://${url}:9093/api/v2/silence/${silenceid}"
    }
}

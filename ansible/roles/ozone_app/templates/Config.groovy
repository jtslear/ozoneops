environments {
    production {
        dataSource {
            dbCreate = "none"
            username = "{{ ansible_local.ozone.dataSource.username }}"
            password = "{{ ansible_local.ozone.dataSource.password }}"
            driverClassName = "{{ app_db_properties[ansible_local.ozone.app.dataSource.type].driver }}"
            url = "{{ app_db_properties[ansible_local.ozone.app.dataSource.type].url_prefix }}{{ ansible_local.ozone.app.dataSource.host }}/{{ ansible_local.ozone.dataSource.schema }}"
            pooled = true
            properties {
                minEvictableIdleTimeMillis = 180000
                timeBetweenEvictionRunsMillis = 180000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "{{ app_db_properties[ansible_local.ozone.app.dataSource.type].validation_query }}"
            }
        }
        uiperformance.enabled = true

        {% if ansible_local.ozone.app.name == "mp" %}
        elasticSearch {
            path.data = new File(
                    "/opt/ozone_store/searchable-index/"
            ).absolutePath

            /*
            {% if cluster_name is defined %}
            client.mode = 'dataNode'
            discovery.zen.ping.unicast.hosts = '
                {%- for host in groups[cluster_name] if host in groups['appservers'] %}
                    {{- host ~ ":" ~ app_es_port|d('9300') }}{% if not loop.last %},{% endif %}
                {%- endfor %}'
            replicas = {{ (groups[cluster_name] | length) - 1 }}
            {% endif %}
            */
        }
        {% endif %}
    }
}

{% if ansible_local.ozone.app.name == "owf" %}
owf {
    log4jWatchTime = 180000;
    enablePendingApprovalWidgetTagGroup = false
    sendWidgetLoadTimesToServer = true
    publishWidgetLoadTimes = true
    lastLoginDateFormat = 'n/j/Y G:i'
    defaultTheme = "a_default"
    showAccessAlert = "true"
    accessAlertMsg = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla interdum eleifend sapien dignissim malesuada. Sed imperdiet augue vitae justo feugiat eget porta est blandit. Proin ipsum ipsum, rutrum ac gravida in, ullamcorper a augue. Sed at scelerisque augue. Morbi scelerisque gravida sapien ut feugiat. Donec dictum, nisl commodo dapibus pellentesque, enim quam consectetur quam, at dictum dui augue at risus. Ut id nunc in justo molestie semper. Curabitur magna velit, varius eu porttitor et, tempor pulvinar nulla. Nam at tellus nec felis tincidunt fringilla. Nunc nisi sem, egestas ut consequat eget, luctus et nisi. Nulla et lorem odio, vitae pretium ipsum. Integer tellus libero, molestie a feugiat a, imperdiet sit amet metus. Aenean auctor fringilla eros, sit amet suscipit felis eleifend a."
    freeTextEntryWarningMessage=''
    logoutURL = "/logout"
    autoSaveInterval = 900000
    helpFileRegex = '^.*\\.(htm|html|gsp|jsp|pdf|doc|docx|mov|mp4|wmv)$'
    useShims = false
    external{
        themePath = 'classpath:themes'
        helpPath = 'classpath:help'
        jsPluginPath = 'classpath:js-plugins'
    }
    metric {
        enabled = false
        url = 'https://localhost:8443/metric/metric'
    }
    dataguard {
        restrictMessages = true
        auditAllMessages = false
        allowMessagesWithoutAccessLevel = true
        accessLevelCacheTimeout = 3600000
    }
    mpSync {
        enabled = true
        autoCreateWidget = false
    }
}
{% endif %}
println('External Config Completed Successfully.')

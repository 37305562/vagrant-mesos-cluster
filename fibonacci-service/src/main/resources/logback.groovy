import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import net.logstash.logback.appender.LogstashTcpSocketAppender
import net.logstash.logback.encoder.LogstashEncoder
import org.slf4j.MDC

import static ch.qos.logback.classic.Level.*

def appenderList = ["CONSOLE", "logstash"]
def serviceId = System.getenv("srv.name") ?: "undefined"
def logstashHost = System.getenv("LOGSTASH_HOST")

jmxConfigurator()

logger('name.krestjaninoff.mesos', DEBUG)

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%-4relative %d %-5level [ %t ] %-55logger{13} | %m %n"
    }
}

if (null != logstashHost) {
    appender("logstash", LogstashTcpSocketAppender) {
        remoteHost = logstashHost
        port = 5959
        encoder(LogstashEncoder)
    }
}

MDC.put("serviceId", serviceId)
root(INFO, appenderList)


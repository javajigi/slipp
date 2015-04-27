from time import sleep
from fabric.api import run, env,execute
from fabric.operations import local
from fabric.context_managers import shell_env,cd

env.base_dir = "/usr/apps"

projects = {
    "slipp-web": {
        "catalina_home": "%(base_dir)s/tomcat" % {"base_dir": env.base_dir},
        "catalina_base": "%(base_dir)s/projects/slipp-web" % {"base_dir": env.base_dir},
        "releases_path": "%(base_dir)s/projects/slipp-web/releases" % {"base_dir": env.base_dir},
        "hosts": ["localhost"],
        "java_opts": "-Djava.awt.headless=true -Dfile.encoding=UTF-8 -server -Xms512m -Xmx1024m -XX:NewSize=256m -XX:MaxNewSize=256m -XX:+DisableExplicitGC"        
    }    
}

def hostname():
    local('uname -a')

def init(name='slipp-web'):
    env.update(projects[name])
    env.project_name = name

def releases():
    """List a releases made"""
    env.releases = sorted(local('ls -x %(releases_path)s' % { 'releases_path':env.releases_path }).split())
    if len(env.releases) >= 1:
        env.current_revision = env.releases[-1]
        env.current_release = "%(releases_path)s/%(current_revision)s" % { 'releases_path':env.releases_path, 'current_revision':env.current_revision }
    if len(env.releases) > 1:
        env.previous_revision = env.releases[-2]
        env.previous_release = "%(releases_path)s/%(previous_revision)s" % { 'releases_path':env.releases_path, 'previous_revision':env.previous_revision }

def checkout():
    local("git pull")

def build():
    execute(checkout)
    local('mvn -U -Pproduction clean install')

def start():
    with shell_env(
            CATALINA_HOME=env.catalina_home,
            CATALINA_BASE=env.catalina_base,
            JAVA_OPTS=env.java_opts
        ):
        local('set -m; %(catalina_home)s/bin/startup.sh' % {'catalina_home':env.catalina_home})  # tomcat instance start

def stop():
    print('Wait until die')
    trial = 0
    max_trial = 5
    pid_commands = 'ps -ef | grep %(project_name)s | grep -v \'grep\' | awk \'{print $2}\'' % { 'project_name':env.project_name }
    while trial < max_trial:
        print('%(remain_seconds)s seconds remained' % {'remain_seconds':(max_trial - trial)})
        if not run(pid_commands):
            break
        trial += 1
        sleep(1)
    if trial == max_trial:
        print('killing catalina')
        local('kill -9 %(running_catalina_pid)s' % {'running_catalina_pid':run(pid_commands)})

def restart():
    execute(stop)
    execute(start)

def mkreleasedir():
    from time import time
    env.current_release = "%(releases_path)s/%(time).0f" % { 'releases_path':env.releases_path, 'time':time() }
    local("mkdir %(current_release)s" % {'current_release':env.current_release})

def copy():
    execute(mkreleasedir)
    local('cp -r ./target/slipp/. %(current_release)s/' % {'current_release':env.current_release}) # file copy

def symboliclink():
    if not env.has_key('current_release'):
        releases()
    local("ln -nfs %(current_release)s %(catalina_base)s/ROOT" % { 'current_release':env.current_release, 'catalina_base':env.catalina_base })

def showlogs():
    local("tail -500f %(catalina_base)s/logs/catalina.out" % { 'catalina_base':env.catalina_base })

def deploy():
    execute(init)
    execute(build)
    execute(copy)
    execute(stop)
    execute(symboliclink)
    execute(start)
    execute(showlogs)
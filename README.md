A Vagrant based Mesos cluster

This project is about setting up a full-stack Mesos cluster inside a farm of Vagrant virtual machines.
It includes the following technologies:

  * Zookeeper
  * Mesos (+ MesosDns)
  * Marathon (+ HaProxy based LoadBalancer)
  * Docker
  * ELK (ElasticSearch + LogStash + Kibana)

Also there is a little SpringBoot based distributed service ("fibonacci-service") which can be   Dockerized and deployed into the cluster to illustrate its work and produce some logs.

More information about this project in the related [article](http://trustmeiamadeveloper.com/2015/12/17/mesos-as-a-docker-containers-farm/) in my blog.


### Dependencies

Note, you have to install

  * vagrant (>= 1.7.4, tested on Virtualbox=5.0-5.0.10 as a provider)
  * ansible (>= 1.9.4)
  * gradle, java and docker (optional, to build the service)

on your local machine before you can start.


### Getting started

#### Cluster

First of all, open the "Vagrantfile" and study it :) As you can see, we are going to create 3 groups of servers:

  * Mesos masters
  * Mesos nodes/slaves
  * Logs storages

Each group is provided by its own Ansible playbook (see "ansible" folder). The most intersting part here is
"role" folder, where you can find all the installation logic for the specific components.

Anyway, all you need to do to start the party is

```
vagrant up
```

It takes some time to download and install all the stuff... so be patient :)


##### Web UIs

After the job is done you can try to check the following UIs:

 * Mesos    - http://192.168.99.11:5050
 * Marathon - http://192.168.99.11:8080
 * HaProxy  - http://192.168.99.11:9090/haproxy?stats
 * Kibana   - http://192.168.99.24:5601

Update your `/etc/hosts` file according to what you see in the head of the Vagrantfile,
if you want to use short names like `master1`.


#### Service

So, you have a cluster! Now, it's time to test it. For that we have a little SpringBoot
REST service which calculates the value of N-th Fibonacci number. The point is it does it
in a dynamic programming manner, i.e. by invocation of another instances of itself
(which are deployed into the cluster). So, that is a quite interesting example for testing.

A Docker image with the service is already published into DockerHub as `krestjaninoff/fibonacci-service`.
However, you can fix something and build your own image. For that make your changes in the code,
update `buildDocker` task in `build.gradle` file and run

```
gradle buildDocker
```

#### Running on the cluster

To run the image on the cluster you have to perform two steps:

  * deploy the image through [Marathon REST API](https://mesosphere.github.io/marathon/docs/rest-api.html)

For that we have a special ansible playbook (`deploy.yml`). If you are too lazy to run it manually,
consider `deploy.sh` script in the root folder :)

Remember, you can use Marathon UI (the link is above) to observer the deployment progress.

Once the app is deployed you can easily invoke it through HaProxy:

```
http://192.168.99.11:80/5
```

where `5` is number of position in the Fibonacci sequence. Have fun and don't forget to check the logs (the link is above) :)


### Troubleshooting

#### Proxy

If you need a proxy support, perform the following:

```
sudo yum install libvirt-devel
vagrant plugin install vagrant-proxyconf
```

Then update $HOME/.vagrant.d/Vagrantfile to apply your settings for all VMs:

```
Vagrant.configure("2") do |config|
  if Vagrant.has_plugin?("vagrant-proxyconf")
    config.proxy.http     = "http://10.0.0.1:3128"
    config.proxy.https    = "http://10.0.0.1:3128"
    config.proxy.no_proxy = "localhost,127.0.0.1"
  end
end
```

#### Virtualization issues

  * VBox interfaces are always in UNKNOWN state (no IPv4 address): https://www.virtualbox.org/ticket/14526
  * Host-only network is unreachable from the host machine: remove all the virtual interfaces (through VBox GUI) and try again (https://www.virtualbox.org/manual/ch06.html#network_hostonly)

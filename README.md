A Vagrant based Mesos cluster (for academical purposes).


### Dependencies

Note, you have to install:

  * vagrant (+virtualbox)
  * ansible (>= 1.9.4)


### Proxy

If you need proxy support, perform the following:

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

### Packages Version
The following packages were used:

```
[vagrant@master1 ~]$ rpm -qa | grep mesos | sort
mesos-0.25.0-0.2.70.centos701406.x86_64
mesosphere-el-repo-7-1.noarch
mesosphere-zookeeper-3.4.6-0.1.20141204175332.centos7.x86_64
```

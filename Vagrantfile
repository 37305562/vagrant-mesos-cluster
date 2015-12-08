#
# A Vagrant-based Mesos cluster
#

# List of VM and their roles
HOSTS = {
  "masters" => {
    "master1" => "192.168.99.11",
    "master2" => "192.168.99.12",
    "master3" => "192.168.99.13"
  },
  "nodes" => {
    "node1"   => "192.168.99.21",
    "node2"   => "192.168.99.22",
    "node3"   => "192.168.99.23"
  },
  "logs" => {
    "log1"    => "192.168.99.24"
  }
}

Vagrant.configure(2) do |config|
  config.vm.box = "bento/centos-7.1"

  HOSTS['masters'].each_with_index do |host, index|
    config.vm.define host[0] do |machine|
      machine.vm.hostname = host[0]

      # In case of problems clean up all the existing virtual interfaces (see VirtualBox instructions)
      # https://www.virtualbox.org/manual/ch06.html#network_hostonly
      # Mesos is now available at http://192.168.99.11:5050/ (update host's /etc/host file to access VM by its names)
      machine.vm.network "private_network", :ip => host[1]

      machine.vm.provision "ansible" do |ansible|
          ansible.playbook = "provisioning/master.yml"
          ansible.groups = { "masters" => HOSTS['masters'].keys }
          ansible.extra_vars = {
            zk_id: "#{index + 1}",
            zk_host1: "master1",
            zk_host2: "master2",
            zk_host3: "master3",
            mesos_quorum: 2,
            marathon_host1: "master1",
            marathon_host2: "master2",
            marathon_host3: "master3",
            hosts: HOSTS['masters'].merge(HOSTS['nodes'])
          }
      end

    end
  end

  HOSTS['nodes'].each_with_index do |host, index|
    config.vm.define host[0] do |machine|
      machine.vm.hostname = host[0]
      machine.vm.network "private_network", :ip => host[1]

      machine.vm.provision "ansible" do |ansible|
          ansible.playbook = "provisioning/node.yml"
          ansible.groups = { "nodes" => HOSTS['nodes'].keys }
          ansible.extra_vars = {
            zk_host1: "master1",
            zk_host2: "master2",
            zk_host3: "master3",
            es_logs_host: HOSTS['logs'].keys[0]
            hosts: HOSTS['masters'].merge(HOSTS['nodes'])
          }
      end
    end
  end

  HOSTS['logs'].each_with_index do |host, index|
    config.vm.define host[0] do |machine|
      machine.vm.hostname = host[0]
      machine.vm.network "private_network", :ip => host[1]

      machine.vm.provision "ansible" do |ansible|
          ansible.playbook = "provisioning/log.yml"
          ansible.groups = { "logs" => HOSTS['logs'].keys }
          ansible.extra_vars = {
            hosts: HOSTS['logs'])
          }
      end
    end
  end

end

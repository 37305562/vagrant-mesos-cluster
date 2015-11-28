#
# A Vagrant-based Mesos cluster
#

# List of VM and their roles (update provisioning/templates/hosts.j2 in case of changes)
ANSIBLE_GROUPS = {
  "masters" => ["master1", "master2", "master3"],
  "nodes"  => ["node1", "node2"]
}
IP_BASE = "192.168.99."
IP_START = 11

Vagrant.configure(2) do |config|
  config.vm.box = "bento/centos-7.1"

  id = 1
  ip_nmbr = IP_START

  ANSIBLE_GROUPS['masters'].each do |name|
    config.vm.define name do |machine|
      machine.vm.hostname = name

      # be aware about dhcp issues https://github.com/mitchellh/vagrant/issues/2968
      machine.vm.network "private_network", :ip => "#{IP_BASE}#{ip_nmbr++}"

      config.vm.network :forwarded_port, guest: 80, host: 80
      config.vm.network :forwarded_port, guest: 81, host: 81      # HaProxy
      config.vm.network :forwarded_port, guest: 8080, host: 8080  # Mesos
      config.vm.network :forwarded_port, guest: 5050, host: 5050  # Marathon
      config.vm.network :forwarded_port, guest: 8000, host: 8000  # Bamboo

      machine.vm.provision "ansible" do |ansible|
          ansible.playbook = "provisioning/master.yml"
          ansible.groups = ANSIBLE_GROUPS
          ansible.extra_vars = {
            zk_id: "#{id++}",
            zk_host1: "master1",
            zk_host2: "master2",
            zk_host3: "master3",
            mesos_quorum: 2
          }
      end
    end
  end

  ANSIBLE_GROUPS['nodes'].each do |name|
    config.vm.define name do |machine|
      machine.vm.hostname = name
      machine.vm.network "private_network", :ip => "#{IP_BASE}#{ip_nmbr++}"

      machine.vm.provision "ansible" do |ansible|
          ansible.playbook = "provisioning/node.yml"
          ansible.groups = ANSIBLE_GROUPS
          ansible.extra_vars = {
            zk_host1: "master1",
            zk_host2: "master2",
            zk_host3: "master3"
          }
      end
    end
  end

end

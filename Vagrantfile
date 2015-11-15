MASTERS = 3
NODES = 4

ANSIBLE_GROUPS = {
  "masters" => ["master1", "master2", "master3"],
  "nodes"  => ["node1", "node2", "node3", "node4"]
}

Vagrant.configure(2) do |config|
  config.vm.box = "bento/centos-7.1"

  (1..MASTERS).each do |id|
    config.vm.define "master#{id}" do |machine|
      machine.vm.hostname = "master#{id}"
      machine.vm.network "private_network", :ip => "192.168.10.#{id}"

      machine.vm.provision "ansible" do |ansible|
          ansible.playbook = "provisioning/master.yml"
          ansible.groups = ANSIBLE_GROUPS
      end
    end
  end

  (1..NODES).each do |id|
    config.vm.define "node#{id}" do |machine|
      machine.vm.hostname = "node#{id}"
      machine.vm.network "private_network", :ip => "192.168.11.#{id}"

      machine.vm.provision "ansible" do |ansible|
          ansible.playbook = "provisioning/node.yml"
          ansible.groups = ANSIBLE_GROUPS
      end
    end
  end

end

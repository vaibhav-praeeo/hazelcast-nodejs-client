import ClientConnection = require('./ClientConnection');
import HazelcastClient = require('../HazelcastClient');
import Address = require('../Address');
import Q = require('q');

class ClusterService {

    private addresses: Address[];

    private client: HazelcastClient;
    private ready = Q.defer<ClusterService>();
    private ownerConnection: ClientConnection;

    constructor(client: HazelcastClient) {
        this.client = client;
        this.addresses = client.getConfig().networkConfig.addresses;
    }

    start(): Q.Promise<ClusterService> {
        this.tryAddress(0);
        return this.ready.promise;
    }

    private tryAddress(index: number) {
        if (index >= this.addresses.length) {
            var error = new Error('Unable to connect to any of the following addresses ' + this.addresses);
            this.ready.reject(error);
        }

        var currentAddress = this.addresses[index];

        this.client.getConnectionManager().getOrConnect(currentAddress).then((connection: ClientConnection) => {
            this.ownerConnection = connection;
            this.ready.resolve(this);
        }).catch((e) => {
            console.log('An error occurred while connecting to: ' + currentAddress);
            console.log(e);
            this.tryAddress(index + 1);
        });
    }

    getOwnerConnection(): ClientConnection {
        return this.ownerConnection;
    }
}

export = ClusterService
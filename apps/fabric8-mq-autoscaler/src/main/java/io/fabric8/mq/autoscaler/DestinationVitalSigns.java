/*
 *
 *  * Copyright 2005-2014 Red Hat, Inc.
 *  * Red Hat licenses this file to you under the Apache License, version
 *  * 2.0 (the "License"); you may not use this file except in compliance
 *  * with the License.  You may obtain a copy of the License at
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  * implied.  See the License for the specific language governing
 *  * permissions and limitations under the License.
 *
 */

package io.fabric8.mq.autoscaler;

import org.apache.activemq.command.ActiveMQDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DestinationVitalSigns {
    private static final Logger LOG = LoggerFactory.getLogger(DestinationVitalSigns.class);

    public enum Type {QUEUE, TOPIC}

    ;

    private int queueDepth;
    private int numberOfProducers;
    private int numberOfConsumers;
    private final ActiveMQDestination destination;

    public DestinationVitalSigns(ActiveMQDestination destination) {
        this.destination = destination;
    }

    public ActiveMQDestination getDestination() {
        return destination;
    }

    public int getNumberOfConsumers() {
        return numberOfConsumers;
    }

    public void setNumberOfConsumers(int numberOfConsumers) {
        this.numberOfConsumers = numberOfConsumers;
    }

    public int getNumberOfProducers() {
        return numberOfProducers;
    }

    public void setNumberOfProducers(int numberOfProducers) {
        this.numberOfProducers = numberOfProducers;
    }

    public int getQueueDepth() {
        return queueDepth;
    }

    public void setQueueDepth(int queueDepth) {
        this.queueDepth = queueDepth;
    }

    public boolean areLimitsExceeded(BrokerVitalSigns brokerVitalSigns, DestinationLimits destinationLimits) {
        int depth = getQueueDepth();
        boolean depthExceeded = depth > destinationLimits.getDepthLimit();
        if (depthExceeded) {
            LOG.info(getDestination() + " exceeded depth limit(" + destinationLimits.getDepthLimit() + ") with " + depth + " on broker" + brokerVitalSigns.getBrokerName());
        }

        int consumers = getNumberOfConsumers();
        boolean consumersExceeded = consumers > destinationLimits.getConsumersLimit();

        if (consumersExceeded) {
            LOG.info(getDestination() + " exceeded consumer limit(" + destinationLimits.getConsumersLimit() + ") with " + consumers + " on broker" + brokerVitalSigns.getBrokerName());
        }

        int producers = getNumberOfProducers();
        boolean producersExceeded = producers > destinationLimits.getProducersLimit();

        if (producersExceeded) {
            LOG.info(getDestination() + " exceeded producer limit(" + destinationLimits.getProducersLimit() + ") with " + producers + " on broker" + brokerVitalSigns.getBrokerName());
        }

        return producersExceeded || consumersExceeded || depthExceeded;
    }

    public String toString() {
        String result = destination.getPhysicalName() + ":,depth=" + getQueueDepth() + ",producers=" + getNumberOfProducers() + ",consumers=" + getNumberOfConsumers();
        return result;
    }

}

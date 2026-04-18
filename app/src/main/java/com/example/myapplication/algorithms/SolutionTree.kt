package com.example.myapplication.algorithms

import kotlin.math.log2

sealed class DecisionTreeNode {
    data class Internal(
        val attribute: String,
        val branches: Map<String, DecisionTreeNode>
    ) : DecisionTreeNode()

    data class Leaf(
        val prediction: String
    ) : DecisionTreeNode()
}

typealias Instance = Map<String, String>

class DecisionTreeModel {
    private var tree: DecisionTreeNode? = null

    var trainingData: List<Instance> = emptyList()
        private set
    var attributes: List<String> = emptyList()
        private set
    var targetAttribute: String = ""
        private set

    public fun train(data: List<Instance>, attributes: List<String>, target: String) {
        this.trainingData = data
        this.attributes = attributes
        this.targetAttribute = target
        tree = buildTree(data, attributes, target)
    }

    public fun retrain() {
        if (trainingData.isNotEmpty() && attributes.isNotEmpty() && targetAttribute.isNotEmpty()) {
            tree = buildTree(trainingData, attributes, targetAttribute)
        }
    }

    public fun addTrainingInstance(instance: Instance) {
        trainingData = trainingData + instance
        retrain()
    }

    public fun predict(instance: Instance): String? {
        return tree?.let { predict(it, instance) }
    }
    private fun entropy(data: List<Instance>, target: String): Double {
        if (data.isEmpty()) return 0.0

        val total = data.size
        val classCounts =
            data.groupingBy { it[target] ?: error("Missing target value") }.eachCount()

        var ent = 0.0
        for (count in classCounts.values) {
            val p = count.toDouble() / total
            ent -= p * log2(p)
        }
        return ent
    }

    private fun informationGain(
        data: List<Instance>,
        attribute: String,
        target: String,
        currentEntropy: Double
    ): Double {
        val total = data.size
        val subsets = data.groupBy { it[attribute] ?: error("Missing subset value") }

        var weightedEntropy = 0.0
        for ((_, subset) in subsets) {
            val weight = subset.size.toDouble() / total
            weightedEntropy += weight * entropy(subset, target)
        }
        return currentEntropy - weightedEntropy
    }

    private fun majorityClass(data: List<Instance>, target: String): String {
        return data.groupingBy { it[target] ?: error("Missing target value") }
            .eachCount()
            .maxByOrNull { it.value }!!
            .key
    }

    private fun buildTree(
        data: List<Instance>,
        attributes: List<String>,
        target: String
    ): DecisionTreeNode {
        val targetValues = data.map { it[target] ?: error("Missing target value") }.distinct()
        if (targetValues.size == 1) {
            return DecisionTreeNode.Leaf(targetValues.first())
        }

        if (attributes.isEmpty()) {
            return DecisionTreeNode.Leaf(majorityClass(data, target))
        }

        val currentEnt = entropy(data, target)
        var bestAttribute: String? = null
        var maxGain = Double.NEGATIVE_INFINITY

        for (attr in attributes) {
            val gain = informationGain(data, attr, target, currentEnt)
            if (gain > maxGain) {
                maxGain = gain
                bestAttribute = attr
            }
        }

        val root = DecisionTreeNode.Internal(
            attribute = bestAttribute!!,
            branches = mutableMapOf()
        )

        val subsets = data.groupBy { it[bestAttribute] } //? возможно баг

        val remainingAttributes = attributes.filter { it != bestAttribute }

        val branches = mutableMapOf<String, DecisionTreeNode>()
        for ((value, subset) in subsets) {
            branches[value ?: "unknown"] = buildTree(subset, remainingAttributes, target)
        }

        return DecisionTreeNode.Internal(bestAttribute, branches)
    }

    private fun predict(node: DecisionTreeNode, instance: Instance): String {
        return when (node) {
            is DecisionTreeNode.Leaf -> node.prediction
            is DecisionTreeNode.Internal -> {
                val attrValue = instance[node.attribute] ?: error("Missing target value")
                if (node.branches.containsKey(attrValue)) {
                    predict(node.branches[attrValue]!!, instance)
                } else {
                    predict(node.branches.values.first(), instance)
                }
            }
        }
    }
}
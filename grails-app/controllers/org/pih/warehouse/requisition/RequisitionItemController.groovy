/**
* Copyright (c) 2012 Partners In Health.  All rights reserved.
* The use and distribution terms for this software are covered by the
* Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
* which can be found in the file epl-v10.html at the root of this distribution.
* By using this software in any fashion, you are agreeing to be bound by
* the terms of this license.
* You must not remove this notice, or any other, from this software.
**/ 
package org.pih.warehouse.requisition

class RequisitionItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [requisitionItemInstanceList: RequisitionItem.list(params), requisitionItemInstanceTotal: RequisitionItem.count()]
    }

    def create = {
        def requisitionItemInstance = new RequisitionItem()
        requisitionItemInstance.properties = params
        return [requisitionItemInstance: requisitionItemInstance]
    }

    def save = {
        def requisitionItemInstance = new RequisitionItem(params)
        if (requisitionItemInstance.save(flush: true)) {
            flash.message = "${warehouse.message(code: 'default.created.message', args: [warehouse.message(code: 'requisitionItem.label', default: 'RequisitionItem'), requisitionItemInstance.id])}"
            redirect(action: "list", id: requisitionItemInstance.id)
        }
        else {
            render(view: "create", model: [requisitionItemInstance: requisitionItemInstance])
        }
    }

    def show = {
        def requisitionItemInstance = RequisitionItem.get(params.id)
        if (!requisitionItemInstance) {
            flash.message = "${warehouse.message(code: 'default.not.found.message', args: [warehouse.message(code: 'requisitionItem.label', default: 'RequisitionItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            [requisitionItemInstance: requisitionItemInstance]
        }
    }

    def edit = {
        def requisitionItemInstance = RequisitionItem.get(params.id)
        if (!requisitionItemInstance) {
            flash.message = "${warehouse.message(code: 'default.not.found.message', args: [warehouse.message(code: 'requisitionItem.label', default: 'RequisitionItem'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [requisitionItemInstance: requisitionItemInstance]
        }
    }

    def update = {
        def requisitionItemInstance = RequisitionItem.get(params.id)
        if (requisitionItemInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (requisitionItemInstance.version > version) {
                    
                    requisitionItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [warehouse.message(code: 'requisitionItem.label', default: 'RequisitionItem')] as Object[], "Another user has updated this RequisitionItem while you were editing")
                    render(view: "edit", model: [requisitionItemInstance: requisitionItemInstance])
                    return
                }
            }
            requisitionItemInstance.properties = params
            if (!requisitionItemInstance.hasErrors() && requisitionItemInstance.save(flush: true)) {
                flash.message = "${warehouse.message(code: 'default.updated.message', args: [warehouse.message(code: 'requisitionItem.label', default: 'RequisitionItem'), requisitionItemInstance.id])}"
                redirect(action: "list", id: requisitionItemInstance.id)
            }
            else {
                render(view: "edit", model: [requisitionItemInstance: requisitionItemInstance])
            }
        }
        else {
            flash.message = "${warehouse.message(code: 'default.not.found.message', args: [warehouse.message(code: 'requisitionItem.label', default: 'RequisitionItem'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def requisitionItemInstance = RequisitionItem.get(params.id)
        if (requisitionItemInstance) {
            try {
                requisitionItemInstance.delete(flush: true)
                flash.message = "${warehouse.message(code: 'default.deleted.message', args: [warehouse.message(code: 'requisitionItem.label', default: 'RequisitionItem'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${warehouse.message(code: 'default.not.deleted.message', args: [warehouse.message(code: 'requisitionItem.label', default: 'RequisitionItem'), params.id])}"
                redirect(action: "list", id: params.id)
            }
        }
        else {
            flash.message = "${warehouse.message(code: 'default.not.found.message', args: [warehouse.message(code: 'requisitionItem.label', default: 'RequisitionItem'), params.id])}"
            redirect(action: "list")
        }
    }
}

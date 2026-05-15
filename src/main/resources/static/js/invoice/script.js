import {makeRequestToBackend, cleanTableBody} from "../utils/common.js";

const txtProductCode = document.getElementById("txtProductCode");
const tblInvoiceLines = document.getElementById("tblInvoiceLines");
const btnSearchProduct = document.getElementById("btnSearchProduct");
const btnCreateInvoice = document.getElementById("btnCreateInvoice");
const pInvoiceTotal = document.getElementById("pInvoiceTotal");
const productEndpoint = "/api/v1/products";
const invoiceEndpoint = "/api/v1/invoices";

async function getProductByCode(txtInput) {
    const code = txtInput.value;
    if(code.length < 1) return;

    const url = productEndpoint+"/"+code;
    const response = await fetch(url);
    const data = await response.json();

    if(!response.ok) {
        alert(data.message);
        throw new Error("Sth went wrong: " + data);
    }

    //txtInput.value = "";
    //txtInput.focus();
    return data;
}

function getColumnValuesFromTable(table, cellIndex){
    let column = [];
    const tbody = table.tBodies[0];
    const rows = Array.from(tbody.rows);
    rows.forEach( row => {
        const cell = row.cells[cellIndex];
            if(cell){
                column.push(cell);
            }
        }
    );
    return column;
}

function createInputQuantity(){
    const inputQuantity = document.createElement("input");
    inputQuantity.type = "text";
    inputQuantity.value = "1";
    inputQuantity.classList.add("form-control", "input-quantity");
    inputQuantity.style.maxWidth = "80px";

    return inputQuantity;
}

function calculateInvoiceLineTotal(quantity, price){
    if(
        quantity === '' ||
        quantity < 1 ||
        typeof quantity === 'undefined' ||
        quantity === null
    ) quantity = 0;
    return Number(quantity * price);
}

function calculateInvoiceTotal(linesTotal){
    let total = Number();
    linesTotal.forEach(
        line => {
            total += Number(line.textContent);
        }
    );
    return total;
}

function setInvoiceTotalValue(table, cellIndex){
    const invoiceLineTotalCells = getColumnValuesFromTable(table, cellIndex);
    pInvoiceTotal.textContent = calculateInvoiceTotal(invoiceLineTotalCells);
}

function checkIfProductWasInserted(targetCode, table) {
    const rows = table.rows;
    for(let i = 0; i < rows.length; i++){
        const existingCode = rows[i].cells[0].textContent.trim();
        if(existingCode === targetCode) return i;
    }
    return -1;
}

function updateQuantityOnMatch(table, rowIndex, newQuantity){
    const inputQuantity = table.rows[rowIndex].cells[3].querySelector('input');
    const oldQuantity = Number(inputQuantity.value);
    inputQuantity.value = oldQuantity + newQuantity;
    const price = table.rows[rowIndex].cells[2].textContent;
    //Update price cell
    table.rows[rowIndex].cells[4].textContent = calculateInvoiceLineTotal(Number(price), Number(inputQuantity.value));
}

function updateQuantityOnUserInput(event){
    const inputQuantity = event.target;
    if(!inputQuantity.classList.contains("input-quantity")) return;
    const row = inputQuantity.closest("tr");
    const price = row.cells[2].textContent;
    row.cells[4].textContent = calculateInvoiceLineTotal(Number(inputQuantity.value), Number(price));

}

function addInvoiceLineToTable(table, product){
    const tbody = table.tBodies[0];
    const newRow = tbody.insertRow();
    newRow.insertCell(0).textContent = product.code;
    newRow.insertCell(1).textContent = product.name;
    newRow.insertCell(2).textContent = product.price;
    newRow.insertCell(3).appendChild(createInputQuantity()); //TODO: Add input of quantity with default value of 1
    newRow.insertCell(4).textContent = product.price; //TODO: Calculate price * quantity and put here the total
}

function checkQuantityValues(table, columnIndex){
    const tbody = table.tBodies[0];
    const rows = Array.from(tbody.rows);

    for (const row of rows){
        const input = row.cells[columnIndex].querySelector('input');
        const quantity = Number(input.value) || 0;

        row.classList.remove('table-danger');
        if(quantity < 1){
            row.classList.add('table-danger');
            alert(`There are invalid quantities`);
            return false;

        }
    }

    return true;
}

function createInvoiceRequest(table){
    const tbody = table.tBodies[0];
    const rows = Array.from(tbody.rows);
    if(rows.length < 1){
        alert('Invoice must have at least one product in its body.');
        return;
    }

    const lines = [];
    let line = {};

    rows.forEach( row => {
        const productCode = row.cells[0].textContent
        const quantity = row.cells[3].querySelector('input').value
        line = {
            'productCode': productCode,
            'quantity': quantity
        }
        lines.push(line);
    });
    const clientName = 'John Doe';
    return {
        'clientName': clientName,
        'lines': lines
    };
}

btnSearchProduct.addEventListener('click', async e => {
    const product = await getProductByCode(txtProductCode);
    if(!product) return;
    const rowIndex = checkIfProductWasInserted(product.code, tblInvoiceLines);
    if(rowIndex >= 0) {
        updateQuantityOnMatch(tblInvoiceLines, rowIndex, 1);
        setInvoiceTotalValue(tblInvoiceLines, 4);
        return;
    }
    addInvoiceLineToTable(tblInvoiceLines, product);
    setInvoiceTotalValue(tblInvoiceLines, 4);
});

btnCreateInvoice.addEventListener('click', async e => {
   if(!checkQuantityValues(tblInvoiceLines, 3)) return;
    const invoiceRequest = createInvoiceRequest(tblInvoiceLines);
    const data = makeRequestToBackend(invoiceEndpoint, invoiceRequest, 'POST');

    if(data) {
        tblInvoiceLines.tBodies[0].innerHTML = "";
        pInvoiceTotal.textContent = ""
    }
});

document.addEventListener('input', e => {
    updateQuantityOnUserInput(e);
    setInvoiceTotalValue(tblInvoiceLines, 4);
});
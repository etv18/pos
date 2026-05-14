import { makeRequestToBackend, inputIsEmpty, setupCurrencyInput } from "../utils/common.js";

const createProductModalElem = document.getElementById("createProductModal");
const updateProductModalElem = document.getElementById("updateProductModal");
const btnSaveNewProduct = document.getElementById("btnSaveNewProduct");
const btnSaveExistingProduct = document.getElementById("btnSaveExistingProduct");

// INPUTS OF CREATE PRODUCT MODAL
const txtName = document.getElementById("txtName");
const txtStock = document.getElementById("txtStock");
const txtPrice = document.getElementById("txtPrice");

// INPUTS/ELEMENTS OF UPDATE PRODUCT MODAL
const txtUpdateName = document.getElementById("txtUpdateName");
const txtUpdatePrice = document.getElementById("txtUpdatePrice");
const txtUpdateStock = document.getElementById("txtUpdateStock");
const spanProductCode = document.getElementById("spanProductCode");

const tblProducts = document.getElementById("tblProducts");

const productsUrl = "/api/v1/products";

async function createProduct(){

    const productRequest = {
        name: txtName.value,
        stock: cleanNumber(txtStock.value),
        price: cleanNumber(txtPrice.value)
    }
    const errorMessage = "Not valid inputs";
    return await makeRequestToBackend(productsUrl, productRequest, "POST", errorMessage);
}

async function updateProduct(){
    const updateProductRequest = {
        code: spanProductCode.textContent,
        name: txtUpdateName.value,
        stock: cleanNumber(txtUpdateStock.value),
        price: cleanNumber(txtUpdatePrice.value)
    }

    return await makeRequestToBackend(productsUrl, updateProductRequest, "PUT");
}

async function deleteProduct(event){
    const target = event.target.closest(".delete-product");
    if(!target) return;
    const code = target.getAttribute("data-product-code");
    const fullUrl = productsUrl + "/" + code;
    const response = await fetch(fullUrl, {method: "DELETE"});
    const data = response.json();

    if(!response.ok) {
        alert("Something went wrong: " + data.message);
        return;
    }
    window.location.reload();
}

function showEditModal(event){
    const target = event.target.closest(".edit-product");
    if(!target) return;
    const modal = new bootstrap.Modal(updateProductModalElem);

    spanProductCode.textContent = target.getAttribute("data-product-code");
    txtUpdateName.value = target.getAttribute("data-product-name");
    txtUpdateStock.value = target.getAttribute("data-product-stock");
    txtUpdatePrice.value = target.getAttribute("data-product-price");

    modal.show();
}

function validateFields(inputs){
    let areValid;
    inputs.forEach(input => {
        areValid = evaluateInput(input);
    });
    return areValid;
}

function evaluateInput(input){
    if(inputIsEmpty(input)){
        input.classList.add("is-invalid");
        return false;
    }
    input.classList.remove("is-invalid");
    return true;
}

function setUpNumberFormatOnInputs(inputs){
    inputs.forEach(input => setupCurrencyInput(input));
}

function cleanNumber(value){
    return value.trim().replace(/,/g, "");
}

btnSaveNewProduct.addEventListener("click", async e => {
    const inputsForCreating = [txtName, txtStock, txtPrice];
    if(!validateFields(inputsForCreating)) return;
    const productResponse = await createProduct();
    if(productResponse) window.location.reload();
});

btnSaveExistingProduct.addEventListener("click", async e => {
    const productResponse = await updateProduct();
    if(productResponse) window.location.reload();
});

tblProducts.addEventListener("click", async e => {
    showEditModal(e);
    await deleteProduct(e);
});

document.addEventListener("DOMContentLoaded", e => {
    const numberInputs = [txtPrice, txtStock, txtUpdateStock, txtUpdatePrice];
    setUpNumberFormatOnInputs(numberInputs);
});



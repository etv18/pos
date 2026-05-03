import { makeRequestToBackend } from "../utils/common.js";

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
        stock: txtStock.value,
        price: txtPrice.value
    }

    return await makeRequestToBackend(productsUrl, productRequest, "POST");
}

async function updateProduct(){
    const updateProductRequest = {
        code: spanProductCode.textContent,
        name: txtUpdateName.value,
        stock: txtUpdateStock.value,
        price: txtUpdatePrice.value
    }

    return await makeRequestToBackend(productsUrl, updateProductRequest, "PUT");
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

btnSaveNewProduct.addEventListener("click", async e => {
    const productResponse = await createProduct();
    if(productResponse) window.location.reload();
});

btnSaveExistingProduct.addEventListener("click", async e => {
    const productResponse = await updateProduct();
    if(productResponse) window.location.reload();
});

tblProducts.addEventListener("click", e => {
    showEditModal(e);
});

createProductModalElem.addEventListener("hidden.bs.modal", () => {

});


const file = document.getElementById('file');
const scanButton = document.getElementById('scan');
const spinner1 = document.getElementById('spinner1');
const spinner2 = document.getElementById('spinner2');
const API_URL = 'https://api.ocr.space/parse/image';
const API_KEY = '6d34fbb89388957';

spinner1.style.display = 'none';
spinner2.style.display = 'none';

scanButton.addEventListener('click', () => {
  let check = sessionStorage.getItem('register');
  if (check) {
    window.location.href =
      'http://localhost:4502/content/hackathon/en/register.html';
  } else {
    let input = file.files[0];
    if (input) {
      spinner1.style.display = 'block';
      makeOCRRequest(input);
    }
  }
});

async function makeOCRRequest(file) {
  const formData = new FormData();
  formData.append('file', file);

  let data = await makeRequest(formData);
  if (data.ParsedResults) {
    // render(data.ParsedResults[0].TextOverlay.Lines);
    validate(data.ParsedResults[0], formData);
  } else {
    console.log(data);
    spinner1.style.display = 'none';
    spinner2.style.display = 'none';
    alert('Cannot scan this image');
  }
}

async function makeRequest(formData) {
  const headers = new Headers({
    apikey: API_KEY
  });
  formData.append('isOverlayRequired', true);
  try {
    let response = await fetch(API_URL, {
      method: 'POST',
      headers,
      body: formData
    });
    if (!response.ok) {
      throw new Error('Some error occured');
    } else {
      spinner.style.display = 'none';
      let data = await response.json();
      return data;
    }
  } catch (error) {
    console.log(error);
    spinner1.style.display = 'none';
    spinner2.style.display = 'none';
    alert('Network error');
    return null;
  }
}

function validate(data, formData) {
  spinner1.style.display = 'none';
  let parsedText = data.ParsedText;
  let mindStoreIndex = parsedText.indexOf('MindStore');
  let receiptIdIndex = parsedText.indexOf('Receipt id');
  if (mindStoreIndex < 0 || receiptIdIndex < 0) {
    swal('Sorry!', 'Invalid receipt. Please upload a valid receipt', 'error');
  } else {
    const user = {
      name: '',
      email: '',
      phone: '',
      receiptID: '',
      receiptpath: ''
    };
    try {
      const CUSTNAME = 'Customer Name';
      const PHONENO = 'Phone no';
      const EMAIL = 'Email';
      const RECEIPTID = 'Receipt id';
      const PRODUCT1 = 'MindStore-Eggs';
      const PRODUCT2 = 'Mindtree-Milk';

      data.TextOverlay.Lines.forEach(line => {
        let text = line.LineText;
        if (text.indexOf(CUSTNAME) > -1) {
          user.name = text.split(':')[1].trim();
        } else if (text.indexOf(PHONENO) > -1) {
          user.phone = text.split(':')[1].trim();
        } else if (text.indexOf(EMAIL) > -1) {
          user.email = text.split(':')[1].trim();
        } else if (text.indexOf(RECEIPTID) > -1) {
          user.receiptID = text.split(':')[1].trim();
        } else if (text === PRODUCT1) {
          document.cookie = 'Product=' + text;
        } else if (text === PRODUCT2) {
          document.cookie = 'Product=' + text;
        }
      });
      validateReceiptId(formData, user);
    } catch (err) {
      swal('Sorry!', 'Invalid receipt. Please upload a valid receipt', 'error');
    }
  }
}
async function validateReceiptId(formData, user) {
  let receiptID = user.receiptID;
  try {
    let FAKE_API_URL = 'https://abhility-fakedb.glitch.me/receipts';
    let response = await fetch(FAKE_API_URL);
    let data = await response.json();
    let found = false;
    for (let id of data) {
      if (id === receiptID) {
        found = true;
        break;
      }
    }
    if (!found) {
      throw Error('Invalid ID');
    } else {
      uploadToDam(formData, user);
    }
  } catch (err) {
    swal(
      'Sorry!',
      'Invalid receipt ID. Please upload a valid receipt',
      'error'
    );
  }
}
function uploadToDam(formData, user) {
  spinner2.style.display = 'block';
  delete formData.delete('isOverlayRequired');
  $.ajax({
    type: 'POST',
    url: '/bin/updamimages',
    processData: false,
    contentType: false,
    data: formData,
    success: function(path) {
      user.receiptpath = path.trim();
      saveTODB(user);
      sweetAlert('Done', 'Image Added Successfully', 'success'); //display the data returned by the servlet
    },
    error: function(err) {
      spinner1.style.display = 'none';
      spinner2.style.display = 'none';
      swal('Sorry!', 'Error while uploading. Please try again', 'error');
    }
  });
}

async function saveTODB(user) {
  let FAKE_API_URL = 'https://abhility-fakedb.glitch.me/users';
  const headers = new Headers({
    'Content-Type': 'application/json'
  });
  try {
    let response = await fetch(FAKE_API_URL, {
      method: 'POST',
      headers,
      body: JSON.stringify(user)
    });
    if (!response.ok) {
      throw new Error('Some error occured');
    } else {
      let data = await response.json();
      swal('Congrats!', 'You have been nominated for reward', 'success');
      spinner2.style.display = 'none';
    }
  } catch (error) {
    console.log(error);
    spinner1.style.display = 'none';
    spinner2.style.display = 'none';
    alert('Network error');
    return null;
  }
}
